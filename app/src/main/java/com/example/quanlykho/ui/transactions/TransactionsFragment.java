package com.example.quanlykho.ui.transactions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.quanlykho.R;
import com.example.quanlykho.adapter.InvoiceAdapter;
import com.example.quanlykho.databinding.FragmentTransactionsBinding;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;
    private FlowerViewModel viewModel;
    private InvoiceAdapter adapter;
    private List<Invoice> allInvoices = new ArrayList<>();
    private List<Invoice> filteredInvoices = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        
        binding.rvInvoices.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new InvoiceAdapter(filteredInvoices, new InvoiceAdapter.OnInvoiceClickListener() {
            @Override
            public void onInvoiceClick(Invoice invoice) {
                Intent intent = new Intent(getContext(), InvoiceDetailActivity.class);
                intent.putExtra("invoice_json", new Gson().toJson(invoice));
                startActivity(intent);
            }

            @Override
            public void onInvoiceLongClick(Invoice invoice) {
                binding.toolbar.setTitle("Đã chọn: 1");
                binding.fabCreateInvoice.setIconResource(android.R.drawable.ic_menu_close_clear_cancel);
                binding.fabCreateInvoice.setText("Hủy chọn");
            }

            @Override
            public void onSelectionChanged(double totalSelectedAmount, int selectedCount) {
                if (selectedCount == 0) {
                    cancelSelection();
                } else {
                    binding.toolbar.setTitle(String.format(Locale.getDefault(), "Tổng: %,.0f đ (%d)", totalSelectedAmount, selectedCount));
                }
            }
        });
        binding.rvInvoices.setAdapter(adapter);

        viewModel.getInvoices().observe(getViewLifecycleOwner(), invoices -> {
            if (invoices != null) {
                allInvoices.clear();
                allInvoices.addAll(invoices);
                applyFilter();
            }
        });

        binding.chipGroupInvoiceFilter.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilter());

        binding.etSearchInvoice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.fabCreateInvoice.setOnClickListener(v -> {
            if (adapter.isSelectionMode()) {
                cancelSelection();
            } else {
                Intent intent = new Intent(getContext(), CreateInvoiceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void cancelSelection() {
        adapter.setSelectionMode(false);
        binding.toolbar.setTitle("Lịch sử hóa đơn");
        binding.fabCreateInvoice.setIconResource(android.R.drawable.ic_menu_edit);
        binding.fabCreateInvoice.setText("Tạo hóa đơn");
    }

    private void applyFilter() {
        String query = binding.etSearchInvoice.getText().toString().toLowerCase().trim();
        int checkedId = binding.chipGroupInvoiceFilter.getCheckedChipId();
        filteredInvoices.clear();
        
        List<Invoice> temp = allInvoices.stream()
                .filter(i -> i.getCustomerName().toLowerCase().contains(query))
                .collect(Collectors.toList());

        if (checkedId == R.id.chipPaid) {
            filteredInvoices.addAll(temp.stream().filter(Invoice::isPaid).collect(Collectors.toList()));
        } else if (checkedId == R.id.chipUnpaid) {
            filteredInvoices.addAll(temp.stream().filter(i -> !i.isPaid()).collect(Collectors.toList()));
        } else {
            filteredInvoices.addAll(temp);
        }
        
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
