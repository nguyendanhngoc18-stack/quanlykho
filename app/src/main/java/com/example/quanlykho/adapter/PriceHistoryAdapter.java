package com.example.quanlykho.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykho.databinding.ItemPriceHistoryBinding;
import com.example.quanlykho.model.PriceHistory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PriceHistoryAdapter extends RecyclerView.Adapter<PriceHistoryAdapter.ViewHolder> {
    private List<PriceHistory> historyList;

    public PriceHistoryAdapter(List<PriceHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPriceHistoryBinding binding = ItemPriceHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriceHistory history = historyList.get(position);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.binding.tvHistoryDate.setText(sdf.format(new Date(history.getTimestamp())));
        
        holder.binding.tvBuyPriceChange.setText(String.format(Locale.getDefault(), "%,.0f -> %,.0f đ", 
                history.getOldBuyPrice(), history.getNewBuyPrice()));
        
        holder.binding.tvSellPriceChange.setText(String.format(Locale.getDefault(), "%,.0f -> %,.0f đ", 
                history.getOldSellPrice(), history.getNewSellPrice()));
        
        holder.binding.tvHistoryNote.setText("Ghi chú: " + (history.getNote() != null ? history.getNote() : "Không có"));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPriceHistoryBinding binding;
        public ViewHolder(ItemPriceHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
