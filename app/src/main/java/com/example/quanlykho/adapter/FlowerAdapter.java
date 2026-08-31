package com.example.quanlykho.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quanlykho.R;
import com.example.quanlykho.databinding.ItemFlowerBinding;
import com.example.quanlykho.model.Flower;

import java.util.List;
import java.util.Locale;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder> {
    private List<Flower> flowerList;
    private OnFlowerClickListener listener;

    public interface OnFlowerClickListener {
        void onFlowerClick(Flower flower);
    }

    public FlowerAdapter(List<Flower> flowerList, OnFlowerClickListener listener) {
        this.flowerList = flowerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFlowerBinding binding = ItemFlowerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FlowerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowerViewHolder holder, int position) {
        Flower flower = flowerList.get(position);

        holder.binding.tvFlowerName.setText(flower.getFlowerName());
        holder.binding.tvLocation.setText("📍 " + flower.getLocation());
        
        double sellPrice = flower.getSellPrice() != null ? flower.getSellPrice() : 0.0;
        holder.binding.tvSellPrice.setText(String.format(Locale.getDefault(), "Giá bán: %,.0fđ", sellPrice));

        if (flower.getQuantity() != null) {
            holder.binding.tvQuantity.setText(String.format(Locale.getDefault(), "%,.1f", flower.getQuantity()));
            holder.binding.tvQuantityUnit.setText(flower.getUnit());
        } else {
            holder.binding.tvQuantity.setText("--");
            holder.binding.tvQuantityUnit.setText("Chờ kiểm");
        }

        if (flower.getImageFlower() != null && !flower.getImageFlower().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(flower.getImageFlower())
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(holder.binding.ivFlowerThumb);
        } else {
            holder.binding.ivFlowerThumb.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlowerClick(flower);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flowerList == null ? 0 : flowerList.size();
    }

    public static class FlowerViewHolder extends RecyclerView.ViewHolder {
        private final ItemFlowerBinding binding;

        public FlowerViewHolder(ItemFlowerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
