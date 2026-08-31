package com.example.quanlykho.model;

import java.util.List;

public class Invoice {
    private String id;
    private String customerName;
    private List<InvoiceItem> items;
    private Double totalAmount;
    private Long timestamp;
    private String type; // "PURCHASE" (Nhập) or "SALE" (Xuất)

    public Invoice() {}

    public Invoice(String id, String customerName, List<InvoiceItem> items, Double totalAmount, Long timestamp, String type) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public List<InvoiceItem> getItems() { return items; }
    public void setItems(List<InvoiceItem> items) { this.items = items; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
