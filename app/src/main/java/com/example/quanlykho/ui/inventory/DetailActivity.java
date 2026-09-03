package com.example.quanlykho.ui.inventory;

import android.content.Intent;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.quanlykho.R;
import com.example.quanlykho.databinding.ActivityDetailBinding;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.model.PriceHistory;
import com.example.quanlykho.model.Transaction;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import com.google.gson.Gson;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private FlowerViewModel viewModel;
    private Flower currentFlower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);

        setupToolbar();

        String flowerJson = getIntent().getStringExtra("flower_json");
        if (flowerJson != null) {
            currentFlower = new Gson().fromJson(flowerJson, Flower.class);
            displayFlowerInfo(currentFlower);
        }

        binding.btnEditPrice.setOnClickListener(v -> showEditPriceDialog());
        binding.btnViewPriceHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, PriceHistoryActivity.class);
            intent.putExtra("flower_id", currentFlower.getId());
            intent.putExtra("flower_name", currentFlower.getFlowerName());
            startActivity(intent);
        });
    }

    private void showEditPriceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa giá: " + currentFlower.getFlowerName());

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_price, null);
        EditText etBuy = view.findViewById(R.id.etEditBuyPrice);
        EditText etSell = view.findViewById(R.id.etEditSellPrice);
        EditText etNote = view.findViewById(R.id.etEditPriceNote);

        etBuy.setText(String.valueOf(currentFlower.getBuyPrice()));
        etSell.setText(String.valueOf(currentFlower.getSellPrice()));

        builder.setView(view);
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            double newBuy = Double.parseDouble(etBuy.getText().toString());
            double newSell = Double.parseDouble(etSell.getText().toString());
            String note = etNote.getText().toString();

            PriceHistory history = new PriceHistory(null, currentFlower.getId(), 
                    currentFlower.getBuyPrice(), newBuy, 
                    currentFlower.getSellPrice(), newSell, 
                    System.currentTimeMillis(), note);

            currentFlower.setBuyPrice(newBuy);
            currentFlower.setSellPrice(newSell);

            viewModel.updateFlowerPrice(currentFlower, history, (success, message) -> {
                if (success) {
                    displayFlowerInfo(currentFlower);
                    Toast.makeText(this, "Đã cập nhật giá mới", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void displayFlowerInfo(Flower flower) {
        binding.tvDetailName.setText(flower.getFlowerName());
        
        String priceText = String.format(Locale.getDefault(), "Mua: %,.0f đ | Bán: %,.0f đ", 
                flower.getBuyPrice() != null ? flower.getBuyPrice() : 0.0,
                flower.getSellPrice() != null ? flower.getSellPrice() : 0.0);
        binding.tvDetailPrice.setText(priceText);
        
        binding.tvDetailQuantity.setText(String.format(Locale.getDefault(), "%,.1f %s", 
                flower.getQuantity() != null ? flower.getQuantity() : 0.0, 
                flower.getUnit()));
        
        binding.tvDetailLocation.setText(flower.getLocation());
        binding.tvDetailDescription.setText(flower.getDescription() != null ? flower.getDescription() : "Không có mô tả.");

        if (flower.getImageFlower() != null && !flower.getImageFlower().isEmpty()) {
            Glide.with(this)
                    .load(flower.getImageFlower())
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(binding.ivFlowerDetail);
        }
    }
}
