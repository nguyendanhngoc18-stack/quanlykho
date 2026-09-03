package com.example.quanlykho.ui.inventory;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.quanlykho.adapter.PriceHistoryAdapter;
import com.example.quanlykho.databinding.ActivityPriceHistoryBinding;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import java.util.ArrayList;

public class PriceHistoryActivity extends AppCompatActivity {
    private ActivityPriceHistoryBinding binding;
    private FlowerViewModel viewModel;
    private PriceHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPriceHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        
        String flowerId = getIntent().getStringExtra("flower_id");
        String flowerName = getIntent().getStringExtra("flower_name");

        setupToolbar(flowerName);
        setupRecyclerView(flowerId);
    }

    private void setupToolbar(String name) {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử giá: " + name);
            binding.toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void setupRecyclerView(String flowerId) {
        binding.rvPriceHistory.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getPriceHistory(flowerId).observe(this, history -> {
            if (history != null) {
                adapter = new PriceHistoryAdapter(history);
                binding.rvPriceHistory.setAdapter(adapter);
            }
        });
    }
}
