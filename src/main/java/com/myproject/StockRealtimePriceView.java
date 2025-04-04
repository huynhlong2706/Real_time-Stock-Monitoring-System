package com.myproject;

import java.util.HashMap;
import java.util.Map;

public class StockRealtimePriceView implements StockViewer {
    private final Map<String, Double> lastPrices = new HashMap<>();

    @Override
    public void onUpdate(StockPrice stockPrice) {
        // TODO: Implement logic to check if price has changed and log it
        String stockCode = stockPrice.getCode();
        double newPrice = stockPrice.getAvgPrice();
        Double lastPrice = lastPrices.get(stockCode);

        if (lastPrice != null && lastPrice != newPrice) {
            Logger.logRealtime(stockCode, newPrice);
        }
        lastPrices.put(stockCode, newPrice);
    }
}
