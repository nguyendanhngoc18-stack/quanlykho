package com.example.quanlykho.model;

public class Transaction {
    private String id;
    private String flowerId;
    private String flowerName;
    private String type; // "IMPORT" or "EXPORT"
    private Double quantity;
    private Long timestamp;
    private String note;

    public Transaction() {}

    public Transaction(String id, String flowerId, String flowerName, String type, Double quantity, Long timestamp, String note) {
        this.id = id;
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.type = type;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }
    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
