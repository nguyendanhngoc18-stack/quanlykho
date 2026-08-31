package com.example.quanlykho.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykho.databinding.ItemSelectedFlowerBinding;
import com.example.quanlykho.model.InvoiceItem;
import java.util.List;
import java.util.Locale;

public class SelectedItemsAdapter extends RecyclerView.Adapter<SelectedItemsAdapter.ViewHolder> {
    private List<InvoiceItem> items;

    public SelectedItemsAdapter(List<InvoiceItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectedFlowerBinding binding = ItemSelectedFlowerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceItem item = items.get(position);
        holder.binding.tvFlowerName.setText(item.getFlowerName());
        holder.binding.tvPriceQty.setText(String.format(Locale.getDefault(), "%,.0f x %,.1f", item.getUnitPrice(), item.getQuantity()));
        holder.binding.tvSubTotal.setText(String.format(Locale.getDefault(), "%,.0f đ", item.getTotalPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSelectedFlowerBinding binding;
        public ViewHolder(ItemSelectedFlowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
