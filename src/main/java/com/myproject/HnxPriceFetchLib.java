package com.myproject;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class HnxPriceFetchLib {
    private Map<String, HnxData> stockData = new HashMap<>();
    private String filePath;
    private Random random = new Random();

    public HnxPriceFetchLib(String filePath) {
        this.filePath = filePath;
        loadStockData("HNX"); // Load only HOSE stocks
    }

    private void loadStockData(String exchange) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(jsonContent);
            JSONObject stockList = json.getJSONObject(exchange); // Load HOSE or HNX

            for (String stockCode : stockList.keySet()) {
                JSONObject stockInfo = stockList.getJSONObject(stockCode);

                JSONArray timestampsArray = stockInfo.getJSONArray("timestamps");
                JSONArray pricesArray = stockInfo.getJSONArray("prices");
                JSONArray volumesArray = stockInfo.getJSONArray("volumes");

                int size = timestampsArray.length(); // Ensure same size for all arrays
                if (pricesArray.length() != size || volumesArray.length() != size) {
                    System.out.println("[ERROR] Data size mismatch for stock: " + stockCode);
                    continue;
                }

                // Select a random index
                int randomIndex = random.nextInt(size);
                long timestamp = timestampsArray.getLong(randomIndex);
                double price = pricesArray.getDouble(randomIndex);
                int volume = volumesArray.getInt(randomIndex);

                stockData.put(stockCode, new HnxData(stockCode, price, volume, timestamp));
            }
        } catch (IOException | JSONException e) {
            System.out.println("[ERROR] Failed to load stock data: " + e.getMessage());
        }
    }

    public List<HnxData> getPrices(List<String> codes) {
        loadStockData("HNX"); // Refresh data before fetching
        List<HnxData> result = new ArrayList<>();

        for (String code : codes) {
            if (stockData.containsKey(code)) {
                result.add(stockData.get(code));
            }
        }
        return result;
    }
}

