package com.example.quanlykho.model;

public class Flower {
    private String id;
    private String imageFlower;
    private String flowerName;
    private String location;
    private String unit;
    private Double quantity;
    private String category;
    private Double buyPrice;
    private Double sellPrice;
    private String description;

    public Flower() {
    }

    public Flower(String id, String imageFlower, String flowerName, String location, String unit, Double quantity, String category, Double buyPrice, Double sellPrice, String description) {
        this.id = id;
        this.imageFlower = imageFlower;
        this.flowerName = flowerName;
        this.location = location;
        this.unit = unit;
        this.quantity = quantity;
        this.category = category;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.description = description;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getImageFlower() { return imageFlower; }
    public void setImageFlower(String imageFlower) { this.imageFlower = imageFlower; }
    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getBuyPrice() { return buyPrice; }
    public void setBuyPrice(Double buyPrice) { this.buyPrice = buyPrice; }
    public Double getSellPrice() { return sellPrice; }
    public void setSellPrice(Double sellPrice) { this.sellPrice = sellPrice; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
