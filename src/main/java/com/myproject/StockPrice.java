package com.myproject;

public class StockPrice {
    private String code;
    private double avgPrice;
    private int volume;
    private long timestamp;

    public StockPrice(String code, double avgPrice, int volume, long timestamp) {
        this.code = code;
        this.avgPrice = avgPrice;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getCode() { return code; }
    public double getAvgPrice() { return avgPrice; }
    public int getVolume() { return volume; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "StockPrice{" + "code='" + code + '\'' + ", avgPrice=" + avgPrice + ", volume=" + volume + ", timestamp=" + timestamp +'}';
    }   
}

