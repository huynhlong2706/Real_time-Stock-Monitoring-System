package com.myproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    // Placeholder method for students to implement correct logging logic
    public static void notImplementedYet(String functionName) {
        System.out.println("[TODO] " + functionName + " is not implemented yet. Students must complete this function.");
    }

    // Log error when registering viewer
    public static void errorRegister(String stockCode) {
        System.out.println("[WARNING] Error when registering with " + stockCode);
    }

    // Log error when unregistering viewer
    public static void errorUnregister(String stockCode) {
        System.out.println("[WARNING] Error when unregistering with " + stockCode);
    }

    // Log real-time price updates
    public static void logRealtime(String stockCode, double price) {
        System.out.println("[REALTIME] Realtime Price Update: " + stockCode + " is now $" + price);
    }

    // Log ticker summary
    public static void logTicker(String stockCode, double high, double low, double open, double close, int volume, double avgPrice, long timestamp) {
        String formattedTime = formatTimestamp(timestamp);
        System.out.printf("[TICKER] Stock: %-5s | High: %8.2f | Low: %8.2f | Open: %8.2f | Close: %8.2f | Volume: %8d | Avg Price: %8.2f | Timestamp: %s%n",
            stockCode, high, low, open, close, volume, avgPrice, formattedTime);
    }
    

    // Convert Unix timestamp (seconds) to human-readable format
    private static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        return sdf.format(new Date(timestamp * 1000)); // Convert to milliseconds
    }

    // Log alert when price changes significantly
    public static void logAlert(String stockCode, double price) {
        System.out.println("[ALERT] " + stockCode + " price changed significantly to $" + price);
    }
}

