package com.myproject;

import java.util.*;

public class StockFeeder {
    private List<Stock> stockList = new ArrayList<>();
    private Map<String, List<StockViewer>> viewers = new HashMap<>();
    private static StockFeeder instance = null;

    // TODO: Implement Singleton pattern
    private StockFeeder() {}

    public static StockFeeder getInstance() {
        // TODO: Implement Singleton logic
        if (instance == null) {
            instance = new StockFeeder();
        }
        return instance;
    }

    public void addStock(Stock stock) {
        // TODO: Implement adding a stock to stockList
        boolean exists = false;
        for (Stock s : stockList) {
            if (s.getCode().equals(stock.getCode())) {
                exists = true;
                break;
            }
        }
        if(!exists) {
            stockList.add(stock);
        }
    }

    public void registerViewer(String code, StockViewer stockViewer) {
        // TODO: Implement registration logic, including checking stock existence
        boolean exists = false;
        for (Stock s : stockList) {
            if (s.getCode().equals(code)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            Logger.errorRegister(code);
        }

        List<StockViewer> viewerList = viewers.get(code);
        if (viewerList == null) {
            viewerList = new ArrayList<>();
            viewers.put(code, viewerList);
        }
        for(StockViewer viewer : viewerList) {
            if (viewer.getClass().equals(stockViewer.getClass())) {
                Logger.errorRegister(code);
            }
        }

        
        
        if (!viewerList.contains(stockViewer)) {
            viewerList.add(stockViewer);
        }
    }    

    public void unregisterViewer(String code, StockViewer stockViewer) {
        // TODO: Implement unregister logic, including error logging
        boolean stockExists = false;
        for (Stock stock : stockList) {
            if (stock.getCode().equals(code)) {
                stockExists = true;
                break;
            }
        }
        if (!stockExists) {
            Logger.errorUnregister(code);
            return;
        }

        List<StockViewer> viewerList = viewers.get(code);
        if (viewerList == null || !viewerList.contains(stockViewer)) {
            Logger.errorUnregister(code);
            return;
        }

        viewerList.remove(stockViewer);
    }

    public void notify(StockPrice stockPrice) {
        // TODO: Implement notifying registered viewers about price updates
        String code = stockPrice.getCode();
        List<StockViewer> viewerList = viewers.get(code);
        if (viewerList != null) {
            for (StockViewer viewer : viewerList) {
                viewer.onUpdate(stockPrice);
            }
        }
    }
}
