package com.example.quanlykho.repository;

import androidx.lifecycle.MutableLiveData;
import com.example.quanlykho.model.Flower;
import com.example.quanlykho.model.Invoice;
import com.example.quanlykho.model.InvoiceItem;
import com.example.quanlykho.model.Transaction;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.List;

public class FlowerRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MutableLiveData<List<Flower>> getFlowersFromFirestore() {
        MutableLiveData<List<Flower>> liveData = new MutableLiveData<>();
        db.collection("flowers").addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null) {
                List<Flower> flowerList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Flower flower = doc.toObject(Flower.class);
                    flower.setId(doc.getId());
                    flowerList.add(flower);
                }
                liveData.setValue(flowerList);
            }
        });
        return liveData;
    }

    public void addFlower(Flower flower, OnCompleteListener listener) {
        db.collection("flowers").add(flower)
                .addOnSuccessListener(documentReference -> {
                    flower.setId(documentReference.getId());
                    db.collection("flowers").document(documentReference.getId()).set(flower);
                    listener.onComplete(true, null);
                })
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public void updateFlower(Flower flower, OnCompleteListener listener) {
        if (flower.getId() == null) return;
        db.collection("flowers").document(flower.getId()).set(flower)
                .addOnSuccessListener(aVoid -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public void deleteFlower(String flowerId, OnCompleteListener listener) {
        db.collection("flowers").document(flowerId).delete()
                .addOnSuccessListener(aVoid -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public void addTransaction(Transaction transaction, OnCompleteListener listener) {
        db.collection("transactions").add(transaction)
                .addOnSuccessListener(documentReference -> {
                    transaction.setId(documentReference.getId());
                    db.collection("transactions").document(documentReference.getId()).set(transaction);
                    listener.onComplete(true, null);
                })
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public void addInvoice(Invoice invoice, OnCompleteListener listener) {
        WriteBatch batch = db.batch();
        String invoiceId = db.collection("invoices").document().getId();
        invoice.setId(invoiceId);
        batch.set(db.collection("invoices").document(invoiceId), invoice);
        
        boolean isPurchase = "PURCHASE".equals(invoice.getType());
        
        for (InvoiceItem item : invoice.getItems()) {
            double qtyChange = isPurchase ? item.getQuantity() : -item.getQuantity();
            
            batch.update(db.collection("flowers").document(item.getFlowerId()), 
                    "quantity", com.google.firebase.firestore.FieldValue.increment(qtyChange));
            
            Transaction t = new Transaction(
                    null, item.getFlowerId(), item.getFlowerName(), 
                    isPurchase ? "IMPORT" : "EXPORT", item.getQuantity(), invoice.getTimestamp(), 
                    (isPurchase ? "Nhập từ: " : "Bán cho: ") + invoice.getCustomerName()
            );
            String tid = db.collection("transactions").document().getId();
            t.setId(tid);
            batch.set(db.collection("transactions").document(tid), t);
        }
        
        batch.commit()
                .addOnSuccessListener(aVoid -> listener.onComplete(true, null))
                .addOnFailureListener(e -> listener.onComplete(false, e.getMessage()));
    }

    public MutableLiveData<List<Invoice>> getInvoices() {
        MutableLiveData<List<Invoice>> liveData = new MutableLiveData<>();
        db.collection("invoices").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        List<Invoice> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            list.add(doc.toObject(Invoice.class));
                        }
                        liveData.setValue(list);
                    }
                });
        return liveData;
    }

    public MutableLiveData<List<Transaction>> getTransactions() {
        MutableLiveData<List<Transaction>> liveData = new MutableLiveData<>();
        db.collection("transactions").orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) {
                        List<Transaction> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            list.add(doc.toObject(Transaction.class));
                        }
                        liveData.setValue(list);
                    }
                });
        return liveData;
    }

    public interface OnCompleteListener {
        void onComplete(boolean success, String message);
    }
}
