package com.example.quanlykho.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
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
    private List<Invoice> invoiceList;

    public InvoiceAdapter(List<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
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
