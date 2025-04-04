package com.myproject;

public class HnxData {
    private String stockCode;
    private double price;
    private int volume;
    private long timestamp;

    public HnxData(String stockCode, double price, int volume, long timestamp) {
        this.stockCode = stockCode;
        this.price = price;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getStockCode() { return stockCode; }
    public double getPrice() { return price; }
    public int getVolume() { return volume; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Stock: %s | Price: %.2f | Volume: %d | Timestamp: %d", stockCode, price, volume, timestamp);
    }
}
