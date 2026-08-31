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
import com.example.quanlykho.adapter.InvoiceAdapter;
import com.example.quanlykho.databinding.FragmentTransactionsBinding;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import java.util.ArrayList;

public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;
    private FlowerViewModel viewModel;
    private InvoiceAdapter adapter;

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
        adapter = new InvoiceAdapter(new ArrayList<>());
        binding.rvInvoices.setAdapter(adapter);

        viewModel.getInvoices().observe(getViewLifecycleOwner(), invoices -> {
            if (invoices != null) {
                adapter = new InvoiceAdapter(invoices);
                binding.rvInvoices.setAdapter(adapter);
            }
        });

        binding.fabCreateInvoice.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateInvoiceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
