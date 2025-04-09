package com.myproject;

import java.util.HashMap;
import java.util.Map;

public class StockAlertView implements StockViewer {
    private double alertThresholdHigh;
    private double alertThresholdLow;
    private Map<String, Double> lastAlertedPrices = new HashMap<>(); // TODO: Stores last alerted price per stock
    // private Map<String, String> lastAlertedTypes = new HashMap<>();

    public StockAlertView(double highThreshold, double lowThreshold) {
        // TODO: Implement constructor
        this.alertThresholdHigh = highThreshold;
        this.alertThresholdLow = lowThreshold;
    }

    @Override
    public void onUpdate(StockPrice stockPrice) {
        // TODO: Implement alert logic based on threshold conditions
        double currentPrice = stockPrice.getAvgPrice();
        String stockCode = stockPrice.getCode();
        synchronized (lastAlertedPrices) {
            if (currentPrice <= alertThresholdLow) {
                if (!lastAlertedPrices.containsKey(stockCode) || 
                    !lastAlertedPrices.get(stockCode).equals(currentPrice)) {
                    alertBelow(stockCode, currentPrice);
                    lastAlertedPrices.put(stockCode, currentPrice);
                }
            }
            // Nếu giá hiện tại vượt ngưỡng báo cao
            else if (currentPrice >= alertThresholdHigh) {
                if (!lastAlertedPrices.containsKey(stockCode) || 
                    !lastAlertedPrices.get(stockCode).equals(currentPrice)) {
                    alertAbove(stockCode, currentPrice);
                    lastAlertedPrices.put(stockCode, currentPrice);
                }
            }
            else {
                lastAlertedPrices.remove(stockCode);
            }
        }
    }

    private void alertAbove(String stockCode, double price) {
        // TODO: Call Logger to log the alert
        Logger.logAlert(stockCode, price);
        // Logger.notImplementedYet("alertAbove");
    }

    private void alertBelow(String stockCode, double price) {
        // TODO: Call Logger to log the alert
        Logger.logAlert(stockCode, price);
        // Logger.notImplementedYet("alertBelow");
    }
}
