package com.example.quanlykho.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykho.databinding.ItemInvoiceBinding;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.model.InvoiceItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {
    public interface OnInvoiceClickListener {
        void onInvoiceClick(Invoice invoice);
        void onInvoiceLongClick(Invoice invoice);
        void onSelectionChanged(double totalSelectedAmount, int selectedCount);
    }

    private List<Invoice> invoiceList;
    private OnInvoiceClickListener listener;
    private boolean isSelectionMode = false;
    private java.util.Set<String> selectedInvoiceIds = new java.util.HashSet<>();

    public InvoiceAdapter(List<Invoice> invoiceList, OnInvoiceClickListener listener) {
        this.invoiceList = invoiceList;
        this.listener = listener;
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!selectionMode) selectedInvoiceIds.clear();
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    private void toggleSelection(Invoice invoice) {
        if (selectedInvoiceIds.contains(invoice.getId())) {
            selectedInvoiceIds.remove(invoice.getId());
        } else {
            selectedInvoiceIds.add(invoice.getId());
        }
        notifyDataSetChanged();
        
        if (listener != null) {
            double total = 0;
            for (Invoice inv : invoiceList) {
                if (selectedInvoiceIds.contains(inv.getId())) {
                    total += inv.getTotalAmount();
                }
            }
            listener.onSelectionChanged(total, selectedInvoiceIds.size());
        }
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInvoiceBinding binding = ItemInvoiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InvoiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);
        
        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                toggleSelection(invoice);
            } else if (listener != null) {
                listener.onInvoiceClick(invoice);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (!isSelectionMode && listener != null) {
                isSelectionMode = true;
                toggleSelection(invoice);
                listener.onInvoiceLongClick(invoice);
                return true;
            }
            return false;
        });

        holder.binding.cbSelect.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);
        holder.binding.cbSelect.setChecked(selectedInvoiceIds.contains(invoice.getId()));
        holder.binding.cbSelect.setOnClickListener(v -> toggleSelection(invoice));

        boolean isPurchase = "PURCHASE".equals(invoice.getType());
        
        String prefix = isPurchase ? "Nhập từ: " : "Khách: ";
        holder.binding.tvCustomerName.setText(prefix + invoice.getCustomerName());
        
        double totalQty = 0;
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) totalQty += item.getQuantity();
        }
        
        int itemCount = invoice.getItems() != null ? invoice.getItems().size() : 0;
        holder.binding.tvItemSummary.setText(itemCount + " loại hoa - Tổng " + totalQty + " bó");
        
        holder.binding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%,.0f đ", invoice.getTotalAmount()));
        holder.binding.tvTotalAmount.setTextColor(isPurchase ? Color.parseColor("#4CAF50") : Color.parseColor("#FF5252"));
        
        // Cập nhật trạng thái thanh toán
        if (invoice.isPaid()) {
            holder.binding.tvPaymentStatus.setText("Đã thanh toán");
            holder.binding.tvPaymentStatus.setBackgroundResource(com.example.quanlykho.R.drawable.bg_status_paid);
        } else {
            holder.binding.tvPaymentStatus.setText("Chưa thanh toán");
            holder.binding.tvPaymentStatus.setBackgroundResource(com.example.quanlykho.R.drawable.bg_status_unpaid);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.binding.tvInvoiceDate.setText(sdf.format(new Date(invoice.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return invoiceList == null ? 0 : invoiceList.size();
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        ItemInvoiceBinding binding;
        public InvoiceViewHolder(ItemInvoiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
