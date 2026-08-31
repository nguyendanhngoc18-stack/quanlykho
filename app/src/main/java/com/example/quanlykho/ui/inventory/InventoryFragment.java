package com.example.quanlykho.ui.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlykho.adapter.FlowerAdapter;
import com.example.quanlykho.databinding.FragmentInventoryBinding;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.viewmodel.FlowerViewModel;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryFragment extends Fragment {
    private FragmentInventoryBinding binding;
    private FlowerAdapter adapter;
    private List<Flower> allFlowers = new ArrayList<>();
    private List<Flower> filteredFlowers = new ArrayList<>();
    private FlowerViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FlowerViewModel.class);
        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        binding.rvInventory.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FlowerAdapter(filteredFlowers, flower -> {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra("flower_json", new Gson().toJson(flower));
            startActivity(intent);
        });
        binding.rvInventory.setAdapter(adapter);

        viewModel.getFlowers().observe(getViewLifecycleOwner(), flowers -> {
            if (flowers != null) {
                allFlowers.clear();
                allFlowers.addAll(flowers);
                applyFilterAndSearch();
            }
        });
    }

    private void setupListeners() {
        binding.fabAddFlower.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddFlowerActivity.class);
            startActivity(intent);
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilterAndSearch();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.chipGroupFilters.setOnCheckedStateChangeListener((group, checkedIds) -> applyFilterAndSearch());
    }

    private void applyFilterAndSearch() {
        String query = binding.etSearch.getText().toString().toLowerCase().trim();
        int checkedChipId = binding.chipGroupFilters.getCheckedChipId();
        String categoryFilter = "";
        
        if (checkedChipId != View.NO_ID) {
            Chip chip = binding.chipGroupFilters.findViewById(checkedChipId);
            if (chip != null && !chip.getText().toString().equals("Tất cả")) {
                categoryFilter = chip.getText().toString();
            }
        }

        final String finalCategory = categoryFilter;
        filteredFlowers.clear();
        List<Flower> temp = allFlowers.stream()
                .filter(f -> f.getFlowerName().toLowerCase().contains(query))
                .filter(f -> finalCategory.isEmpty() || (f.getCategory() != null && f.getCategory().equals(finalCategory)))
                .collect(Collectors.toList());
        
        filteredFlowers.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
