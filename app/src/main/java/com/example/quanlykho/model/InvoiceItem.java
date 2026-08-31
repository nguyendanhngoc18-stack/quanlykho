package com.example.quanlykho.model;

public class InvoiceItem {
    private String flowerId;
    private String flowerName;
    private Double quantity;
    private Double unitPrice;
    private Double totalPrice;

    public InvoiceItem() {}

    public InvoiceItem(String flowerId, String flowerName, Double quantity, Double unitPrice) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = quantity * unitPrice;
    }

    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }
    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
