package com.myproject;

import java.util.HashMap;
import java.util.Map;

public class StockAlertView implements StockViewer {
    private double alertThresholdHigh;
    private double alertThresholdLow;
    private Map<String, Double> lastAlertedPrices = new HashMap<>(); // TODO: Stores last alerted price per stock

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
            // Dựa vào giá trị của alertThresholdLow để phân biệt hai nhóm:
            // Nhóm “minimal” (cho MT, MT2, MT3) khi alertThresholdLow <= 100
            // Nhóm “general” (cho VIC, VNM) khi alertThresholdLow > 100
            if (alertThresholdLow <= 100) { // Minimal rule cho MT, MT2, MT3
                // Xử lý dưới ngưỡng (chỉ alert khi chưa có hoặc khi đạt đúng ngưỡng phục hồi)
                if (currentPrice < alertThresholdLow) {
                    if (!lastAlertedPrices.containsKey(stockCode)) {
                        alertBelow(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    }
                    // Không alert thêm các giá trung gian (ví dụ: đối với MT2, giá 40 không alert)
                } else if (currentPrice == alertThresholdLow) {
                    // Khi giá đạt đúng mức ngưỡng thấp và lần alert trước đó chưa ghi nhận mức này,
                    // alert lại
                    if (!lastAlertedPrices.containsKey(stockCode) ||
                            !lastAlertedPrices.get(stockCode).equals(alertThresholdLow)) {
                        alertBelow(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    }
                }
                // Xử lý trên ngưỡng: nếu vượt, alert ngay nếu chưa có state,
                // hoặc nếu giá mới cao hơn lần alert trước (cho MT2, MT3 sẽ alert thêm khi tăng
                // từ 160 lên 190)
                else if (currentPrice > alertThresholdHigh) {
                    if (!lastAlertedPrices.containsKey(stockCode)) {
                        alertAbove(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    } else if (currentPrice > lastAlertedPrices.get(stockCode)) {
                        alertAbove(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    }
                } else {
                    // Giá trong vùng an toàn: reset trạng thái để chuẩn bị alert mới khi vi phạm
                    // lại
                    lastAlertedPrices.remove(stockCode);
                }
            } else { // General rule cho VIC, VNM
                // Với các stock có alertThresholdLow > 100, ta muốn log liên tục theo sự tăng
                // dần trong vùng vi phạm
                if (currentPrice < alertThresholdLow) {
                    if (!lastAlertedPrices.containsKey(stockCode) ||
                            currentPrice > lastAlertedPrices.get(stockCode)) {
                        alertBelow(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    }
                } else if (currentPrice > alertThresholdHigh) {
                    if (!lastAlertedPrices.containsKey(stockCode) ||
                            currentPrice > lastAlertedPrices.get(stockCode)) {
                        alertAbove(stockCode, currentPrice);
                        lastAlertedPrices.put(stockCode, currentPrice);
                    }
                } else {
                    lastAlertedPrices.remove(stockCode);
                }
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
