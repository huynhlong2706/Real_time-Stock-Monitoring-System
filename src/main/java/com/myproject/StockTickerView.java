package com.myproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class StockTickerView implements StockViewer {
    private final Map<String, StockData> stockDataMap = new HashMap<>();

    public StockTickerView() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                logTickerInfo();
            }
        }, 0, 10 * 1000);
    }

    @Override
    public void onUpdate(StockPrice stockPrice) {
        String stockCode = stockPrice.getCode();
        StockData data = stockDataMap.computeIfAbsent(stockCode, k -> new StockData(stockPrice));
        data.updatePrice(stockPrice);
    }

    private void logTickerInfo() {
        for (Map.Entry<String, StockData> entry : stockDataMap.entrySet()) {
            StockData data = entry.getValue();
            Logger.logTicker(entry.getKey(), data.highest, data.lowest, data.opening, data.closing, data.volume, data.getAveragePrice(), data.timestamp);
        }
    }

    private static class StockData {
        double highest, lowest, opening, closing, totalPrice;
        int volume, cnt;
        long timestamp;

        StockData(StockPrice stockPrice) {
            this.opening = this.highest = this.lowest = this.closing = this.totalPrice = stockPrice.getAvgPrice();
            this.volume = stockPrice.getVolume();
            this.timestamp = stockPrice.getTimestamp();
            this.cnt = 1;
        }

        void updatePrice(StockPrice stockPrice) {
            double newPrice = stockPrice.getAvgPrice();

            if (newPrice != closing) {
                highest = Math.max(highest, newPrice);
                lowest = Math.min(lowest, newPrice);
                closing = newPrice;
                totalPrice += newPrice;
                volume = stockPrice.getVolume();
                timestamp = stockPrice.getTimestamp();
                cnt++;
            }
        }

        double getAveragePrice() {
            return totalPrice / cnt;
        }
    }
}

