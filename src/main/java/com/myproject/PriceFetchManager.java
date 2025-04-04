package com.myproject;

import java.util.*;
import java.util.concurrent.*;

public class PriceFetchManager {
    private final ScheduledExecutorService executor;
    private final StockFeeder stockFeeder;
    private final List<PriceFetcher> fetchers; // List of fetchers

    public PriceFetchManager(StockFeeder stockFeeder) {
        this.stockFeeder = stockFeeder;
        this.fetchers = new ArrayList<>();
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void addFetcher(PriceFetcher fetcher) {
        fetchers.add(fetcher);
    }

    public void run() {
        List<StockPrice> allPrices = new ArrayList<>();
        
        for (PriceFetcher fetcher : fetchers) {
            List<StockPrice> prices = fetcher.fetch();
            allPrices.addAll(prices);
        }

        if (!allPrices.isEmpty()) {
            for (StockPrice stockPrice : allPrices) {
                stockFeeder.notify(stockPrice);
            }
        }
    }

    public void start() {
        executor.scheduleAtFixedRate(this::run, 0, 1, TimeUnit.MINUTES); // Run every 1 minute
    }

    public void stop() {
        executor.shutdown();
    }
}

