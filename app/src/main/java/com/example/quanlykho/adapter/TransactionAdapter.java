package com.example.quanlykho.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlykho.databinding.ItemTransactionBinding;
import com.example.quanlykho.model.Transaction;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction t = transactionList.get(position);
        holder.binding.tvFlowerName.setText(t.getFlowerName());
        
        String typeStr = "XUẤT";
        int color = Color.RED;
        if ("IMPORT".equals(t.getType())) {
            typeStr = "NHẬP";
            color = Color.parseColor("#4CAF50"); // Green
        }
        
        holder.binding.tvType.setText(typeStr);
        holder.binding.tvType.setTextColor(color);
        holder.binding.tvQuantity.setText(String.format(Locale.getDefault(), "%,.1f", t.getQuantity()));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        holder.binding.tvDate.setText(sdf.format(new Date(t.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return transactionList == null ? 0 : transactionList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ItemTransactionBinding binding;
        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
