package com.example.quanlykho.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.quanlykho.R;
import com.example.quanlykho.databinding.ActivityInvoiceDetailBinding;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.model.InvoiceItem;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoiceDetailActivity extends AppCompatActivity {
    private ActivityInvoiceDetailBinding binding;
    private FlowerViewModel viewModel;
    private Invoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInvoiceDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        
        String json = getIntent().getStringExtra("invoice_json");
        if (json != null) {
            invoice = new Gson().fromJson(json, Invoice.class);
            displayInvoiceData();
        }

        setupToolbar();
        setupListeners();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void displayInvoiceData() {
        boolean isPurchase = "PURCHASE".equals(invoice.getType());
        String prefix = isPurchase ? "Nhà cung cấp: " : "Khách hàng: ";
        binding.tvDetailCustomer.setText(prefix + invoice.getCustomerName());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        binding.tvDetailDate.setText("Ngày tạo: " + sdf.format(new Date(invoice.getTimestamp())));
        
        binding.tvDetailTotal.setText(String.format(Locale.getDefault(), "%,.0f đ", invoice.getTotalAmount()));
        
        binding.layoutItemsContainer.removeAllViews();
        if (invoice.getItems() != null) {
            for (InvoiceItem item : invoice.getItems()) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_selected_flower, binding.layoutItemsContainer, false);
                TextView tvName = itemView.findViewById(R.id.tvFlowerName);
                TextView tvInfo = itemView.findViewById(R.id.tvPriceQty);
                TextView tvPrice = itemView.findViewById(R.id.tvSubTotal);
                
                tvName.setText(item.getFlowerName());
                tvInfo.setText("Số lượng: " + item.getQuantity());
                tvPrice.setText(String.format(Locale.getDefault(), "%,.0f đ", item.getTotalPrice()));
                
                binding.layoutItemsContainer.addView(itemView);
                
                // Thêm đường kẻ giữa các dòng hoa
                if (invoice.getItems().indexOf(item) < invoice.getItems().size() - 1) {
                    View divider = new View(this);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(getResources().getColor(R.color.divider));
                    binding.layoutItemsContainer.addView(divider);
                }
            }
        }

        updateStatusUI(invoice.isPaid());
    }

    private void updateStatusUI(boolean isPaid) {
        binding.switchPaymentStatus.setOnCheckedChangeListener(null); // Prevent loop
        binding.switchPaymentStatus.setChecked(isPaid);
        binding.tvStatusLabel.setText(isPaid ? "Đã thanh toán" : "Chưa thanh toán");
        binding.tvStatusLabel.setTextColor(isPaid ? 
                getResources().getColor(R.color.status_fresh) : 
                getResources().getColor(R.color.status_danger));
        
        binding.switchPaymentStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.updateInvoicePaymentStatus(invoice.getId(), isChecked, (success, message) -> {
                if (success) {
                    invoice.setPaid(isChecked);
                    updateStatusUI(isChecked);
                    Toast.makeText(this, "Đã cập nhật trạng thái", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_SHORT).show();
                    binding.switchPaymentStatus.setChecked(!isChecked); // Revert
                }
            });
        });
    }

    private void setupListeners() {
        // Already handled in updateStatusUI
    }
}