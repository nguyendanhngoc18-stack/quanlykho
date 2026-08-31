package com.example.quanlykho.ui.transactions;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlykho.R;
import com.example.quanlykho.adapter.FlowerAdapter;
import com.example.quanlykho.adapter.SelectedItemsAdapter;
import com.example.quanlykho.databinding.ActivityCreateInvoiceBinding;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.model.InvoiceItem;
import com.example.quanlykho.viewmodel.FlowerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CreateInvoiceActivity extends AppCompatActivity {
    private ActivityCreateInvoiceBinding binding;
    private FlowerViewModel viewModel;
    private List<InvoiceItem> selectedItems = new ArrayList<>();
    private SelectedItemsAdapter adapter;
    private double totalAmount = 0;
    private List<Flower> allFlowers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateInvoiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        
        setupRecyclerView();
        
        binding.fabAddItem.setOnClickListener(v -> showFlowerSelectionDialog());
        binding.btnSaveInvoice.setOnClickListener(v -> saveInvoice());
        
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }

        viewModel.getFlowers().observe(this, flowers -> {
            if (flowers != null) allFlowers = flowers;
        });

        binding.toggleInvoiceType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                // Clear current items when switching type to avoid price confusion
                selectedItems.clear();
                totalAmount = 0;
                adapter.notifyDataSetChanged();
                binding.tvTotalAmount.setText("0 đ");
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new SelectedItemsAdapter(selectedItems);
        binding.rvSelectedItems.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSelectedItems.setAdapter(adapter);
    }

    private void showFlowerSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn loại hoa");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_flower, null);
        RecyclerView rv = dialogView.findViewById(R.id.rvSelectFlower);
        EditText etSearch = dialogView.findViewById(R.id.etSearchFlower);

        rv.setLayoutManager(new LinearLayoutManager(this));
        
        List<Flower> displayedFlowers = new ArrayList<>(allFlowers);
        FlowerAdapter flowerAdapter = new FlowerAdapter(displayedFlowers, flower -> {
            // Dismissal handled later in showQuantityDialog
        });
        rv.setAdapter(flowerAdapter);

        AlertDialog dialog = builder.setView(dialogView).create();
        
        // Re-set click listener to handle dialog dismissal
        flowerAdapter = new FlowerAdapter(displayedFlowers, flower -> {
            dialog.dismiss();
            showQuantityDialog(flower);
        });
        rv.setAdapter(flowerAdapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().toLowerCase().trim();
                displayedFlowers.clear();
                displayedFlowers.addAll(allFlowers.stream()
                        .filter(f -> f.getFlowerName().toLowerCase().contains(query))
                        .collect(Collectors.toList()));
                rv.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialog.show();
    }

    private void showQuantityDialog(Flower flower) {
        boolean isSale = binding.toggleInvoiceType.getCheckedButtonId() == R.id.btnTypeSale;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Số lượng cho " + flower.getFlowerName());

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        String hint = isSale ? "Số lượng (hiện có: " + flower.getQuantity() + ")" : "Số lượng nhập";
        input.setHint(hint);
        builder.setView(input);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String qtyStr = input.getText().toString();
            if (!qtyStr.isEmpty()) {
                double qty = Double.parseDouble(qtyStr);
                if (isSale && qty > (flower.getQuantity() != null ? flower.getQuantity() : 0)) {
                    Toast.makeText(this, "Không đủ hàng trong kho!", Toast.LENGTH_SHORT).show();
                } else {
                    addItemToInvoice(flower, qty);
                }
            }
        });
        builder.show();
    }

    private void addItemToInvoice(Flower flower, double qty) {
        boolean isSale = binding.toggleInvoiceType.getCheckedButtonId() == R.id.btnTypeSale;
        double price = isSale ? 
                (flower.getSellPrice() != null ? flower.getSellPrice() : 0) : 
                (flower.getBuyPrice() != null ? flower.getBuyPrice() : 0);
        
        InvoiceItem item = new InvoiceItem(flower.getId(), flower.getFlowerName(), qty, price);
        selectedItems.add(item);
        totalAmount += item.getTotalPrice();
        
        adapter.notifyItemInserted(selectedItems.size() - 1);
        binding.tvTotalAmount.setText(String.format(Locale.getDefault(), "%,.0f đ", totalAmount));
    }

    private void saveInvoice() {
        String name = binding.etCustomerName.getText().toString().trim();
        if (name.isEmpty()) {
            binding.etCustomerName.setError("Nhập tên đối tác");
            return;
        }
        if (selectedItems.isEmpty()) {
            Toast.makeText(this, "Chưa chọn hoa nào!", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = binding.toggleInvoiceType.getCheckedButtonId() == R.id.btnTypeSale ? "SALE" : "PURCHASE";
        Invoice invoice = new Invoice(null, name, selectedItems, totalAmount, System.currentTimeMillis(), type);
        
        viewModel.addInvoice(invoice, (success, message) -> {
            if (success) {
                Toast.makeText(this, "Đã lưu hóa đơn thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
