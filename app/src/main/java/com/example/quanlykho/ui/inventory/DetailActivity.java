package com.example.quanlykho.ui.inventory;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.quanlykho.R;
import com.example.quanlykho.databinding.ActivityDetailBinding;
import com.example.quanlykho.model.Flower;
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

        binding.btnImportMore.setOnClickListener(v -> showTransactionDialog("IMPORT"));
        binding.btnExport.setOnClickListener(v -> showTransactionDialog("EXPORT"));
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

    private void showTransactionDialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type.equals("IMPORT") ? "Nhập thêm hàng" : "Xuất kho (Dùng cho hỏng hóc)");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Nhập số lượng");
        builder.setView(input);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String qtyStr = input.getText().toString();
            if (!qtyStr.isEmpty()) {
                processTransaction(type, Double.parseDouble(qtyStr));
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void processTransaction(String type, double qty) {
        double currentQty = currentFlower.getQuantity() != null ? currentFlower.getQuantity() : 0.0;
        double newQty = type.equals("IMPORT") ? currentQty + qty : currentQty - qty;

        if (newQty < 0) {
            Toast.makeText(this, "Số lượng xuất vượt quá tồn kho!", Toast.LENGTH_SHORT).show();
            return;
        }

        currentFlower.setQuantity(newQty);
        viewModel.updateFlower(currentFlower, (success, message) -> {
            if (success) {
                Transaction transaction = new Transaction(
                        null,
                        currentFlower.getId(),
                        currentFlower.getFlowerName(),
                        type,
                        qty,
                        System.currentTimeMillis(),
                        type.equals("IMPORT") ? "Nhập kho bổ sung" : "Xuất kho (Lý do khác)"
                );
                viewModel.addTransaction(transaction, (s, m) -> {
                    displayFlowerInfo(currentFlower);
                    Toast.makeText(DetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
