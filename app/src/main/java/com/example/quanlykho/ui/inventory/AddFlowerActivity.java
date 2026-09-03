package com.example.quanlykho.ui.inventory;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.quanlykho.databinding.ActivityAddFlowerBinding;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.viewmodel.FlowerViewModel;

import java.util.Map;

public class AddFlowerActivity extends AppCompatActivity {
    private ActivityAddFlowerBinding binding;
    private FlowerViewModel viewModel;
    private Uri imageUri;
    private String uploadedImageUrl = "";

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    binding.ivFlower.setImageURI(imageUri);
                    binding.ivFlower.setPadding(0, 0, 0, 0);
                    binding.ivFlower.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddFlowerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);

        setupToolbar();
        setupCategorySpinner();

        binding.cardImage.setOnClickListener(v -> openImagePicker());
        binding.btnSave.setOnClickListener(v -> saveData());
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void setupCategorySpinner() {
        String[] categories = {"Hoa cắt cành", "Hoa chậu", "Hoa lụa", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        binding.spinnerCategory.setAdapter(adapter);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    private void saveData() {
        String name = binding.etName.getText().toString().trim();
        String category = binding.spinnerCategory.getText().toString().trim();
        String location = binding.etLocation.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String buyPriceStr = binding.etBuyPrice.getText().toString().trim();
        String sellPriceStr = binding.etSellPrice.getText().toString().trim();
        String qtyStr = binding.etQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            binding.etName.setError("Vui lòng nhập tên hoa");
            return;
        }

        double buyPrice = buyPriceStr.isEmpty() ? 0.0 : Double.parseDouble(buyPriceStr);
        double sellPrice = sellPriceStr.isEmpty() ? 0.0 : Double.parseDouble(sellPriceStr);
        double quantity = qtyStr.isEmpty() ? 0.0 : Double.parseDouble(qtyStr);

        if (imageUri != null) {
            uploadImageAndSave(name, category, location, description, buyPrice, sellPrice, quantity);
        } else {
            saveToFirestore(name, category, location, description, buyPrice, sellPrice, quantity, "");
        }
    }

    private void uploadImageAndSave(String name, String category, String location, String description, double buyPrice, double sellPrice, double quantity) {
        binding.layoutProgress.setVisibility(android.view.View.VISIBLE);
        MediaManager.get().upload(imageUri)
                .option("upload_preset", "KhoHoa")
                .option("unsigned", true)
                .callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {}

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {}

            @Override
            public void onSuccess(String requestId, Map resultData) {
                uploadedImageUrl = (String) resultData.get("secure_url");
                saveToFirestore(name, category, location, description, buyPrice, sellPrice, quantity, uploadedImageUrl);
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                binding.layoutProgress.setVisibility(android.view.View.GONE);
                Toast.makeText(AddFlowerActivity.this, "Tải ảnh thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {}
        }).dispatch();
    }

    private void saveToFirestore(String name, String category, String location, String description, double buyPrice, double sellPrice, double quantity, String imageUrl) {
        binding.layoutProgress.setVisibility(android.view.View.VISIBLE);
        Flower flower = new Flower(null, imageUrl, name, location, "Bó", quantity, category, buyPrice, sellPrice, description);
        viewModel.addFlower(flower, (success, message) -> {
            binding.layoutProgress.setVisibility(android.view.View.GONE);
            if (success) {
                Toast.makeText(AddFlowerActivity.this, "Đã lưu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddFlowerActivity.this, "Lỗi: " + message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
