package com.example.quanlykho.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlykho.databinding.FragmentDashboardBinding;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.viewmodel.FlowerViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private FlowerViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);

        viewModel.getFlowers().observe(getViewLifecycleOwner(), this::updateFlowerStats);
        viewModel.getInvoices().observe(getViewLifecycleOwner(), this::updateInvoiceStats);
    }

    private void updateFlowerStats(List<Flower> flowers) {
        if (flowers == null) return;
        double totalStock = 0;
        Set<String> categories = new HashSet<>();
        for (Flower flower : flowers) {
            if (flower.getQuantity() != null) totalStock += flower.getQuantity();
            if (flower.getCategory() != null) categories.add(flower.getCategory());
        }
        binding.tvTotalStock.setText(String.valueOf((int)totalStock));
        binding.tvCategoriesCount.setText(String.valueOf(categories.size()));
    }

    private void updateInvoiceStats(List<Invoice> invoices) {
        if (invoices == null) return;
        binding.tvInvoicesCount.setText(String.valueOf(invoices.size()));
        
        double totalRevenue = 0;
        binding.containerRecentInvoices.removeAllViews();
        
        int limit = Math.min(invoices.size(), 5);
        for (int i = 0; i < limit; i++) {
            Invoice inv = invoices.get(i);
            totalRevenue += inv.getTotalAmount();
            addInvoiceToDashboard(inv);
        }
        
        // Show total revenue from recent or all? Let's show total from all invoices
        double grandTotal = 0;
        for (Invoice inv : invoices) grandTotal += inv.getTotalAmount();
        
        if (grandTotal >= 1000000) {
            binding.tvMonthlyRevenue.setText(String.format(Locale.getDefault(), "%.1fM", grandTotal / 1000000.0));
        } else {
            binding.tvMonthlyRevenue.setText(String.format(Locale.getDefault(), "%.0fK", grandTotal / 1000.0));
        }
    }

    private void addInvoiceToDashboard(Invoice inv) {
        View view = getLayoutInflater().inflate(com.example.quanlykho.R.layout.item_transaction, binding.containerRecentInvoices, false);
        TextView name = view.findViewById(com.example.quanlykho.R.id.tvFlowerName);
        TextView amount = view.findViewById(com.example.quanlykho.R.id.tvQuantity);
        TextView date = view.findViewById(com.example.quanlykho.R.id.tvDate);
        TextView type = view.findViewById(com.example.quanlykho.R.id.tvType);

        name.setText(inv.getCustomerName());
        amount.setText(String.format(Locale.getDefault(), "%,.0fđ", inv.getTotalAmount()));
        type.setText("HĐ");
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        date.setText(sdf.format(new java.util.Date(inv.getTimestamp())));
        
        binding.containerRecentInvoices.addView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
