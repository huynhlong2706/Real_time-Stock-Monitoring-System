package com.myproject;

import java.util.ArrayList;
import java.util.List;

public class HnxAdapter implements PriceFetcher {
    private HnxPriceFetchLib hnxLib;
    private List<String> stockCodes; // List of stock codes to fetch

    public HnxAdapter(HnxPriceFetchLib hnxLib, List<String> stockCodes) {
        this.hnxLib = hnxLib;
        this.stockCodes = stockCodes;
    }

    @Override
    public List<StockPrice> fetch() {
        List<StockPrice> stockPrices = new ArrayList<>();

        List<HnxData> hnxDataList = hnxLib.getPrices(stockCodes);
        for (HnxData hnxData : hnxDataList) {
            stockPrices.add(convertToStockPrice(hnxData));
        }

        return stockPrices;
    }

    private StockPrice convertToStockPrice(HnxData hnxData) {
        return new StockPrice(hnxData.getStockCode(), hnxData.getPrice(), hnxData.getVolume(), hnxData.getTimestamp());
    }
}

