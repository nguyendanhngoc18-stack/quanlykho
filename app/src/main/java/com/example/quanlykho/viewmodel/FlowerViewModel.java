package com.example.quanlykho.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.model.Transaction;
import com.example.quanlykho.repository.FlowerRepository;
import java.util.List;

public class FlowerViewModel extends ViewModel {
    private FlowerRepository repository = new FlowerRepository();
    private LiveData<List<Flower>> flowerList;

    public LiveData<List<Flower>> getFlowers() {
        if (flowerList == null) {
            flowerList = repository.getFlowersFromFirestore();
        }
        return flowerList;
    }

    public void addFlower(Flower flower, FlowerRepository.OnCompleteListener listener) {
        repository.addFlower(flower, listener);
    }

    public void updateFlower(Flower flower, FlowerRepository.OnCompleteListener listener) {
        repository.updateFlower(flower, listener);
    }

    public void deleteFlower(String flowerId, FlowerRepository.OnCompleteListener listener) {
        repository.deleteFlower(flowerId, listener);
    }

    public void addTransaction(Transaction transaction, FlowerRepository.OnCompleteListener listener) {
        repository.addTransaction(transaction, listener);
    }

    public void addInvoice(Invoice invoice, FlowerRepository.OnCompleteListener listener) {
        repository.addInvoice(invoice, listener);
    }

    public LiveData<List<Invoice>> getInvoices() {
        return repository.getInvoices();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return repository.getTransactions();
    }
}
