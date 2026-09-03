package com.example.quanlykho.model;

public class PriceHistory {
    private String id;
    private String flowerId;
    private Double oldBuyPrice;
    private Double newBuyPrice;
    private Double oldSellPrice;
    private Double newSellPrice;
    private Long timestamp;
    private String note;

    public PriceHistory() {}

    public PriceHistory(String id, String flowerId, Double oldBuyPrice, Double newBuyPrice, 
                        Double oldSellPrice, Double newSellPrice, Long timestamp, String note) {
        this.id = id;
        this.flowerId = flowerId;
        this.oldBuyPrice = oldBuyPrice;
        this.newBuyPrice = newBuyPrice;
        this.oldSellPrice = oldSellPrice;
        this.newSellPrice = newSellPrice;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFlowerId() { return flowerId; }
    public void setFlowerId(String flowerId) { this.flowerId = flowerId; }
    public Double getOldBuyPrice() { return oldBuyPrice; }
    public void setOldBuyPrice(Double oldBuyPrice) { this.oldBuyPrice = oldBuyPrice; }
    public Double getNewBuyPrice() { return newBuyPrice; }
    public void setNewBuyPrice(Double newBuyPrice) { this.newBuyPrice = newBuyPrice; }
    public Double getOldSellPrice() { return oldSellPrice; }
    public void setOldSellPrice(Double oldSellPrice) { this.oldSellPrice = oldSellPrice; }
    public Double getNewSellPrice() { return newSellPrice; }
    public void setNewSellPrice(Double newSellPrice) { this.newSellPrice = newSellPrice; }
    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
