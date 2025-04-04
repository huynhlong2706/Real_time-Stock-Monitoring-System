package com.myproject;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static StockFeeder stockFeeder;
    private static PriceFetchManager priceFetchManager;

    public static void main(String[] args) {
        init();
        run();
    }

    private static void init() {
        stockFeeder = StockFeeder.getInstance();

        String filePath = "src/resources/stocks.json";

        HosePriceFetchLib hoseLib = new HosePriceFetchLib(filePath);
        List<String> hoseStockCodes = Arrays.asList("VIC", "VNM", "HPG", "FPT");
        HoseAdapter hoseAdapter = new HoseAdapter(hoseLib, hoseStockCodes);

        HnxPriceFetchLib hnxLib = new HnxPriceFetchLib(filePath);
        List<String> hnxStockCodes = Arrays.asList("SHB", "ACB", "PVS");
        HnxAdapter hnxAdapter = new HnxAdapter(hnxLib, hnxStockCodes);

        priceFetchManager = new PriceFetchManager(stockFeeder);
        priceFetchManager.addFetcher(hoseAdapter);
        priceFetchManager.addFetcher(hnxAdapter);

        Stock VICstock = new Stock("VIC", "Vingroup");
        stockFeeder.addStock(VICstock); 

        stockFeeder.registerViewer("VIC", new StockAlertView(60000, 55000));
        stockFeeder.registerViewer("VIC", new StockRealtimePriceView());
        stockFeeder.registerViewer("VIC", new StockTickerView());

        priceFetchManager.start(); // Start fetching stock prices
    }

    private static void run() {
        try {
            Thread.sleep(Long.MAX_VALUE); // Keeps the program running
        } catch (InterruptedException e) {
            priceFetchManager.stop();
            System.out.println("[ERROR] Stock monitoring service interrupted.");
        }
    }
}

// package com.myproject;

// import java.io.ByteArrayOutputStream;
// import java.io.PrintStream;
// import java.lang.reflect.Field;

// class TestStockAlertView extends StockAlertView {
//     public TestStockAlertView(double highThreshold, double lowThreshold) {
//         super(highThreshold, lowThreshold);
//     }

//     @Override
//     public synchronized void onUpdate(StockPrice stockPrice) {
//         // Sử dụng synchronized để đảm bảo chỉ một luồng được xử lý tại 1 thời điểm
//         super.onUpdate(stockPrice);
//     }
// }

// class TestStockRealtimePriceView extends StockRealtimePriceView {
//     public TestStockRealtimePriceView() {
//         super();
//     }

//     @Override
//     public synchronized void onUpdate(StockPrice stockPrice) {
//         // Đồng bộ hóa để tránh race condition khi cập nhật lastPrices
//         super.onUpdate(stockPrice);
//     }
// }

// public class Main {
//     private static int count_pass_test = 0;
//     private static int num_test = 30;

//     private static final String ANSI_RESET = "\u001B[0m";
//     private static final String ANSI_RED = "\u001B[31m";
//     private static final String ANSI_GREEN = "\u001B[32m";

//     private static StockTickerView tickerView;

//     public static void main(String[] args) {
//         resetStockFeeder();
//         StockFeeder_Testcase_01();
//         resetStockFeeder();
//         StockFeeder_Testcase_02();
//         resetStockFeeder();
//         StockFeeder_Testcase_03();
//         resetStockFeeder();
//         StockFeeder_Testcase_04();
//         resetStockFeeder();
//         StockFeeder_Testcase_05();
//         resetStockFeeder();
//         StockFeeder_Testcase_06();
//         resetStockFeeder();
//         StockFeeder_Testcase_07();
//         resetStockFeeder();
//         StockFeeder_Testcase_08();
//         resetStockFeeder();
//         StockFeeder_Testcase_09();

//         resetStockFeeder();
//         StockAlertView_Testcase_01();
//         resetStockFeeder();
//         StockAlertView_Testcase_02();
//         resetStockFeeder();
//         StockAlertView_Testcase_03();
//         resetStockFeeder();
//         StockAlertView_Testcase_04();
//         resetStockFeeder();
//         StockAlertView_Testcase_05();
//         resetStockFeeder();
//         StockAlertView_Testcase_06();
//         resetStockFeeder();
//         StockAlertView_Testcase_07();
//         resetStockFeeder();
//         StockAlertView_Testcase_08();
//         resetStockFeeder();
//         StockAlertView_Testcase_09();

//         Realtime_Testcase_01();
//         Realtime_Testcase_02();
//         Realtime_Testcase_03();
//         Realtime_Testcase_04();

//         FeederRealtime_Testcase_01();
//         FeederRealtime_Testcase_02();
//         FeederRealtime_Testcase_03();
//         FeederRealtime_Testcase_04();

//         Summary_Testcase_01();
//         Summary_Testcase_02();
//         Summary_Testcase_03();
//         Summary_Testcase_04();

//         printResult();
//     }

//     // Test Singleton property
//     private static void StockFeeder_Testcase_01() {
//         StockFeeder instance1 = StockFeeder.getInstance();
//         StockFeeder instance2 = StockFeeder.getInstance();
//         if (instance1 == instance2) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_01: Get Instance" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_01: Get Instance" + ANSI_RESET);
//         }
//     }

//     // Test Private Constructor (Reflection)
//     private static void StockFeeder_Testcase_02() {
//         try {
//             StockFeeder.class.getDeclaredConstructor().setAccessible(true);
//             StockFeeder instance = StockFeeder.class.getDeclaredConstructor().newInstance();
//             System.out.println(instance);
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_02: Private Constructor" + ANSI_RESET);
//         } catch (Exception e) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_02: Private Constructor" + ANSI_RESET);
//             count_pass_test++;
//         }
//     }

//     private static void StockFeeder_Testcase_03() {
//         final StockFeeder[] instances = new StockFeeder[2];
//         Thread t1 = new Thread(() -> instances[0] = StockFeeder.getInstance());
//         Thread t2 = new Thread(() -> instances[1] = StockFeeder.getInstance());

//         t1.start();
//         t2.start();

//         try {
//             t1.join();
//             t2.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }

//         if (instances[0] == instances[1]) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_03: Compare Instance" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_03: Compare Instance" + ANSI_RESET);
//         }
//     }

//     private static void StockFeeder_Testcase_04() {
//         StockFeeder instance1 = StockFeeder.getInstance();
//         StockFeeder instance2 = StockFeeder.getInstance();
//         StockFeeder instance3 = StockFeeder.getInstance();

//         if (instance1 == instance2 && instance2 == instance3) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_04: Compare MultiInstance" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_04: Compare MultiInstance" + ANSI_RESET);
//         }
//     }

//     private static void StockFeeder_Testcase_05() {
//         StockFeeder instance = StockFeeder.getInstance();
//         if (instance instanceof StockFeeder) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_05: Correct Instance" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_05: Correct Instance" + ANSI_RESET);
//         }
//     }

//     // Test 9: Register với mã không tồn tại
//     private static void StockFeeder_Testcase_06() {
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("LTNC", "Lap Trinh Nang Cao"));
//             StockAlertView view1 = new StockAlertView(100, 50);
//             feeder.registerViewer("LTNC_VT", view1);
//         });
//         if (output.contains("[WARNING] Error when registering with LTNC_VT")) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_06: Error Register" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_06: Error Register" + ANSI_RESET);
//         }
//     }

//     // Test 9: Unregister với mã không tồn tại
//     private static void StockFeeder_Testcase_07() {
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("LTNC", "Lap Trinh Nang Cao"));
//             StockAlertView view1 = new StockAlertView(100, 50);
//             feeder.registerViewer("LTNC", view1);
//             feeder.unregisterViewer("LTNC_VT", view1);
//         });
//         if (output.contains("[WARNING] Error when unregistering with LTNC_VT")) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_07: Error Unregister" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_07: Error Unregister" + ANSI_RESET);
//         }
//     }

//     // Test 8: Add Stock successfully (stock không tồn tại trong danh sách)
//     private static void StockFeeder_Testcase_08() {
//         StockFeeder feeder = StockFeeder.getInstance();
//         int initialSize = getStockListSize(feeder);
//         Stock newStock = new Stock("NEW", "New Stock");
//         feeder.addStock(newStock);
//         int newSize = getStockListSize(feeder);
//         if (newSize == initialSize + 1) {
//             System.out.println(ANSI_GREEN + "Pass StockFeeder_Testcase_08: Add Stock successfully" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockFeeder_Testcase_08: Add Stock successfully" + ANSI_RESET);
//         }
//     }

//     // Test 9: Add Stock failed (nếu stock đã tồn tại thì không được thêm lại)
//     private static void StockFeeder_Testcase_09() {
//         StockFeeder feeder = StockFeeder.getInstance();
//         int initialSize = getStockListSize(feeder);
//         Stock duplicateStock = new Stock("DUP", "Duplicate Stock");
//         feeder.addStock(duplicateStock);
//         // Thêm lần đầu thành công
//         feeder.addStock(duplicateStock);
//         int newSize = getStockListSize(feeder);
//         // Nếu duplicate không được thêm thì kích thước danh sách tăng 1 so với ban đầu
//         if (newSize == initialSize + 1) {
//             System.out.println(
//                     ANSI_GREEN + "Pass StockFeeder_Testcase_09: Add Stock failed (duplicate not added)" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(
//                     ANSI_RED + "Fail StockFeeder_Testcase_09: Add Stock failed (duplicate not added)" + ANSI_RESET);
//         }
//     }

//     // Test 1: Kiểm tra constructor của StockAlertView
//     private static void StockAlertView_Testcase_01() {
//         try {
//             StockAlertView view = new StockAlertView(100, 50);
//             if (view instanceof StockAlertView) {
//                 System.out.println(ANSI_GREEN + "Pass StockAlertView_Testcase_01" + ANSI_RESET);
//                 count_pass_test++;
//             } else {
//                 System.out.println(ANSI_RED + "Fail StockAlertView_Testcase_01" + ANSI_RESET);
//             }
//         } catch (Exception e) {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 1: Constructor" + ANSI_RESET);
//         }
//     }

//     // Test 2: Khi giá vượt ngưỡng cao thì alert được gọi
//     private static void StockAlertView_Testcase_02() {
//         String output = captureOutput(() -> {
//             StockAlertView view = new StockAlertView(100, 50);
//             StockPrice sp = new StockPrice("VT-LTNC", 105, 1000, 1711368000);
//             view.onUpdate(sp);
//         });
//         // Logger in ra định dạng: "[ALERT] ABC price changed significantly to $105.0"
//         if (output.contains("[ALERT] VT-LTNC price changed significantly to $105.0")) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 2: Alert Above" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 2: Alert Above" + ANSI_RESET);
//         }
//     }

//     // Test 3: Khi giá dưới ngưỡng thấp thì alert được gọi
//     private static void StockAlertView_Testcase_03() {
//         String output = captureOutput(() -> {
//             StockAlertView view = new StockAlertView(100, 50);
//             StockPrice sp = new StockPrice("VT", 45, 1000, 1711368888);
//             view.onUpdate(sp);
//         });
//         if (output.contains("[ALERT] VT price changed significantly to $45.0")) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 3: Alert Below" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 3: Alert Below" + ANSI_RESET);
//         }
//     }

//     // Test 4: Nếu giá nằm trong khoảng [low, high] thì không có alert
//     private static void StockAlertView_Testcase_04() {
//         String output = captureOutput(() -> {
//             StockAlertView view = new StockAlertView(100, 50);
//             StockPrice sp = new StockPrice("LMN", 75, 1000, 0);
//             view.onUpdate(sp);
//         });
//         if (!output.contains("[ALERT] LMN price changed significantly to $75.0")) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 4: No Alert In Range" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 4: No Alert In Range" + ANSI_RESET);
//         }
//     }

//     // Test 5: Nếu cùng một giá được update nhiều lần thì chỉ log 1 lần alert
//     private static void StockAlertView_Testcase_05() {
//         String output = captureOutput(() -> {
//             StockAlertView view = new StockAlertView(100, 50);
//             StockPrice sp = new StockPrice("ABC", 105, 1000, 0);
//             view.onUpdate(sp);
//             view.onUpdate(sp);
//         });
//         int occurrences = countOccurrences(output, "[ALERT] ABC price changed significantly to $105.0");
//         if (occurrences == 1) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 5: Duplicate Alert Not Logged" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 5: Duplicate Alert Not Logged" + ANSI_RESET);
//         }
//     }

//     // Test 6: Đăng ký StockAlertView với StockFeeder, sau đó notify => phải có
//     // alert
//     private static void StockAlertView_Testcase_06() {
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("VT", "Vo Tien"));
//             StockAlertView view = new StockAlertView(100, 50);
//             feeder.registerViewer("VT", view);
//             StockPrice sp = new StockPrice("VT", 110, 1000, 0);
//             feeder.notify(sp);
//         });
//         if (output.contains("[ALERT] VT price changed significantly to $110.0")) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 6: Register & Notify" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 6: Register & Notify" + ANSI_RESET);
//         }
//     }

//     // Test 7: Đăng ký nhiều StockAlertView cho cùng một mã, notify => phải log
//     // alert từ tất cả các view
//     private static void StockAlertView_Testcase_07() {
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("LTNC", "Lap Trinh Nang Cao"));
//             StockAlertView view1 = new StockAlertView(100, 50);
//             StockAlertView view2 = new StockAlertView(100, 50);
//             feeder.registerViewer("LTNC", view1);
//             feeder.registerViewer("LTNC", view2);
//             StockPrice sp = new StockPrice("LTNC", 105, 1000, 0);
//             feeder.notify(sp);
//         });
//         int occurrences = countOccurrences(output, "[ALERT] LTNC price changed significantly to $105.0");
//         if (occurrences == 2) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 7: Multi Viewer" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 7: Multi Viewer" + ANSI_RESET);
//         }
//     }

//     // Test 8: Gỡ đăng ký StockAlertView => sau notify không có alert từ view đó
//     private static void StockAlertView_Testcase_08() {
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("LTNC-VT", "Lap Trinh Nang Cao - Vo Tien"));
//             StockAlertView view = new StockAlertView(100, 50);
//             feeder.registerViewer("LTNC-VT", view);
//             feeder.unregisterViewer("LTNC-VT", view);
//             StockPrice sp = new StockPrice("LTNC-VT", 105, 1000, 0);
//             feeder.notify(sp);
//         });
//         if (!output.contains("[ALERT] LTNC-VT price changed significantly to $105.0")) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView Test 8: Unregister Viewer" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail StockAlertView Test 8: Unregister Viewer" + ANSI_RESET);
//         }
//     }

//     // Test 9: Multi-threaded Observer Test cho StockFeeder sử dụng
//     // TestStockAlertView với đồng bộ hóa
//     private static void StockAlertView_Testcase_09() {
//         resetStockFeeder(); // Reset StockFeeder để test độc lập
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("MT", "MultiThread Stock"));
//             feeder.addStock(new Stock("MT2", "MultiThread Stock 2"));
//             feeder.addStock(new Stock("MT3", "MultiThread Stock 3"));
//             // Dùng lớp TestStockAlertView có đồng bộ hóa nội bộ
//             StockAlertView view = new TestStockAlertView(100, 50);
//             StockAlertView view2 = new TestStockAlertView(150, 70);
//             feeder.registerViewer("MT", view);
//             feeder.registerViewer("MT2", view2);
//             feeder.registerViewer("MT3", view2);

//             // Sử dụng ExecutorService với 3 luồng, không cần thêm lock ngoài nữa vì
//             // TestStockAlertView đã đồng bộ hóa
//             java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(3);

//             // Luồng 1: Cập nhật giá 120 (vượt ngưỡng cao) 5 lần
//             Runnable task1 = () -> {
//                 for (int i = 2; i <= 6; i++) {
//                     feeder.notify(new StockPrice("MT", 20 * i, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             // Luồng 2: Cập nhật giá 40 (dưới ngưỡng thấp) 5 lần
//             Runnable task2 = () -> {
//                 for (int i = 0; i <= 6; i++) {
//                     feeder.notify(new StockPrice("MT2", 30 * i + 10, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             // Luồng 3: Cập nhật giá 80 (trong khoảng, không có alert) 5 lần
//             Runnable task3 = () -> {
//                 for (int i = 0; i <= 6; i++) {
//                     feeder.notify(new StockPrice("MT3", 30 * i + 10, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             executor.submit(task1);
//             executor.submit(task2);
//             executor.submit(task3);
//             executor.shutdown();
//             while (!executor.isTerminated()) {
//                 try {
//                     Thread.sleep(10);
//                 } catch (InterruptedException e) {
//                 }
//             }
//         });
//         // Với TestStockAlertView, ta mong đợi:
//         // - Khi giá 120 (vượt ngưỡng cao): chỉ log 1 lần alert
//         // - Khi giá 40 (dưới ngưỡng thấp): chỉ log 1 lần alert
//         int occurrencesAbove = countOccurrences(output, "[ALERT] MT price changed significantly to $120.0");
//         occurrencesAbove += countOccurrences(output, "[ALERT] MT price changed significantly to $100.0");
//         occurrencesAbove += countOccurrences(output, "[ALERT] MT2 price changed significantly to $160.0");
//         occurrencesAbove += countOccurrences(output, "[ALERT] MT3 price changed significantly to $160.0");
//         occurrencesAbove += countOccurrences(output, "[ALERT] MT2 price changed significantly to $190.0");
//         occurrencesAbove += countOccurrences(output, "[ALERT] MT3 price changed significantly to $190.0");

//         int occurrencesBelow = countOccurrences(output, "[ALERT] MT price changed significantly to $40.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT2 price changed significantly to $10.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT3 price changed significantly to $10.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT2 price changed significantly to $40.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT3 price changed significantly to $40.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT2 price changed significantly to $70.0");
//         occurrencesBelow += countOccurrences(output, "[ALERT] MT3 price changed significantly to $70.0");

//         if (occurrencesAbove == 5 && occurrencesBelow == 5) {
//             System.out.println(ANSI_GREEN + "Pass StockAlertView_Testcase_09: Multi-threaded Observer" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail StockAlertView_Testcase_09: Multi-threaded Observer" + ANSI_RESET);
//         }
//     }

//     // Test 1: Khi chưa có giá, update lần đầu ⇒ phải log realtime update
//     private static void Realtime_Testcase_01() {
//         String output = captureOutput(() -> {
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             StockPrice sp = new StockPrice("ABC", 5000, 1000, 0);
//             view.onUpdate(sp);
//         });
//         output += "First Update Log";
//         if (output.equals("First Update Log")) {
//             System.out.println(ANSI_GREEN + "Pass Realtime_Testcase_01: First Update Log" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail Realtime_Testcase_01: First Update Log" + ANSI_RESET);
//         }
//     }

//     // Test 2: Cập nhật liên tiếp cùng giá ⇒ không log lại
//     private static void Realtime_Testcase_02() {
//         String output = captureOutput(() -> {
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             StockPrice sp = new StockPrice("ABC", 5000, 1000, 0);
//             view.onUpdate(sp);
//             sp = new StockPrice("ABC", 5007, 1008, 3600);
//             view.onUpdate(sp);
//             view.onUpdate(sp);
//         });
//         int occurrences = countOccurrences(output, "[REALTIME] Realtime Price Update: ABC is now $5007.0");
//         if (occurrences == 1) {
//             System.out.println(ANSI_GREEN + "Pass Realtime_Testcase_02: No Duplicate Log" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail Realtime_Testcase_02: No Duplicate Log" + ANSI_RESET);
//         }
//     }

//     // Test 3: Khi giá thay đổi ⇒ phải log update mới
//     private static void Realtime_Testcase_03() {
//         String output = captureOutput(() -> {
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             StockPrice sp1 = new StockPrice("ABC", 5000, 1000, 0);
//             StockPrice sp2 = new StockPrice("ABC", 5100, 1000, 0);
//             view.onUpdate(sp1);
//             view.onUpdate(sp2);
//             sp1 = new StockPrice("ABC", 10000, 1000, 3600);
//             sp2 = new StockPrice("ABC", 100, 1000, 3600);
//             view.onUpdate(sp1);
//             view.onUpdate(sp2);
//         });
//         if (output.contains("[REALTIME] Realtime Price Update: ABC is now $10000.0") &&
//                 output.contains("[REALTIME] Realtime Price Update: ABC is now $5100.0")) {
//             System.out.println(ANSI_GREEN + "Pass Realtime_Testcase_03: Log Update on Change" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail Realtime_Testcase_03: Log Update on Change" + ANSI_RESET);
//         }
//     }

//     // Test 4: Cập nhật cho 2 stock khác nhau ⇒ mỗi stock đều log
//     private static void Realtime_Testcase_04() {
//         String output = captureOutput(() -> {
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             StockPrice sp1 = new StockPrice("ABC", 5000, 1000, 0);
//             StockPrice sp2 = new StockPrice("XYZ", 6000, 1000, 0);
//             view.onUpdate(sp1);
//             view.onUpdate(sp2);
//             sp1 = new StockPrice("ABC", 5600, 1000, 0);
//             sp2 = new StockPrice("XYZ", 1000, 1000, 0);
//             view.onUpdate(sp1);
//             view.onUpdate(sp2);
//         });
//         if (output.contains("[REALTIME] Realtime Price Update: XYZ is now $1000.0")
//                 && output.contains("[REALTIME] Realtime Price Update: ABC is now $5600.0")) {
//             System.out.println(ANSI_GREEN + "Pass Realtime_Testcase_04: Multiple Stock Log" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail Realtime_Testcase_04: Multiple Stock Log" + ANSI_RESET);
//         }
//     }

//     // Test 6: Đăng ký viewer, notify cho 1 stock ⇒ log realtime update
//     private static void FeederRealtime_Testcase_01() {
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("DEF", "Stock DEF"));
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             feeder.registerViewer("DEF", view);
//             StockPrice sp = new StockPrice("DEF", 7000, 1000, 0);
//             feeder.notify(sp);
//             sp = new StockPrice("DEF", 7150, 1000, 3600);
//             feeder.notify(sp);
//         });
//         if (output.contains("[REALTIME] Realtime Price Update: DEF is now $7150.0")) {
//             System.out.println(ANSI_GREEN + "Pass FeederRealtime_Testcase_01: Register & Notify" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail FeederRealtime_Testcase_01: Register & Notify" + ANSI_RESET);
//         }
//     }

//     // Test 7: Đăng ký viewer cho 2 stock, notify từng stock
//     private static void FeederRealtime_Testcase_02() {
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("DEF", "Stock DEF"));
//             feeder.addStock(new Stock("GHI", "Stock GHI"));
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             feeder.registerViewer("DEF", view);
//             feeder.registerViewer("GHI", view);
//             feeder.notify(new StockPrice("DEF", 7000, 1000, 0));
//             feeder.notify(new StockPrice("GHI", 8000, 1000, 0));
//             feeder.notify(new StockPrice("DEF", 7, 1000, 0));
//             feeder.notify(new StockPrice("GHI", 8, 1000, 0));
//         });
//         if (output.contains("[REALTIME] Realtime Price Update: DEF is now $7.0") &&
//                 output.contains("[REALTIME] Realtime Price Update: GHI is now $8.0")) {
//             System.out.println(ANSI_GREEN + "Pass FeederRealtime_Testcase_02: Multiple Stocks" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail FeederRealtime_Testcase_02: Multiple Stocks" + ANSI_RESET);
//         }
//     }

//     // Test 8: Đăng ký rồi gỡ đăng ký viewer, notify ⇒ không log
//     private static void FeederRealtime_Testcase_03() {
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             feeder.addStock(new Stock("DEF", "Stock DEF"));
//             StockRealtimePriceView view = new StockRealtimePriceView();
//             feeder.registerViewer("DEF", view);
//             feeder.unregisterViewer("DEF", view);
//             feeder.notify(new StockPrice("DEF", 7000, 1000, 0));
//         });
//         if (!output.contains("[REALTIME] Realtime Price Update: DEF is now $7000.0")) {
//             System.out.println(ANSI_GREEN + "Pass FeederRealtime_Testcase_03: Unregister Viewer" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + "Fail FeederRealtime_Testcase_03: Unregister Viewer" + ANSI_RESET);
//         }
//     }

//     // Test 9: Multi-threaded Realtime Observer Test cho StockFeeder sử dụng
//     // TestStockRealtimePriceView với đồng bộ hóa
//     private static void FeederRealtime_Testcase_04() {
//         resetStockFeeder(); // Reset StockFeeder để test độc lập
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             // Thêm 3 stock vào feeder
//             feeder.addStock(new Stock("RT1", "Realtime Stock 1"));
//             feeder.addStock(new Stock("RT2", "Realtime Stock 2"));
//             feeder.addStock(new Stock("RT3", "Realtime Stock 3"));
//             // Sử dụng TestStockRealtimePriceView có đồng bộ hóa
//             TestStockRealtimePriceView view = new TestStockRealtimePriceView();
//             feeder.registerViewer("RT1", view);
//             feeder.registerViewer("RT2", view);
//             feeder.registerViewer("RT3", view);

//             // Sử dụng ExecutorService với 3 luồng
//             java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(3);

//             // Luồng 1: Cập nhật giá cho RT1: lần đầu là 100, sau đó 105 (nên log 2 lần)
//             Runnable task1 = () -> {
//                 for (int i = 0; i < 5; i++) {
//                     double price = (i == 0) ? 100 : 105;
//                     feeder.notify(new StockPrice("RT1", price, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             // Luồng 2: Cập nhật giá cho RT2: lần đầu là 200, sau đó 210 (nên log 2 lần)
//             Runnable task2 = () -> {
//                 for (int i = 0; i < 5; i++) {
//                     double price = (i == 0) ? 200 : (i == 2 ? 210 : 200);
//                     feeder.notify(new StockPrice("RT2", price, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             // Luồng 3: Cập nhật giá cho RT3: luôn 300 (nên log 1 lần)
//             Runnable task3 = () -> {
//                 for (int i = 0; i < 5; i++) {
//                     feeder.notify(new StockPrice("RT3", 300, 1000, System.currentTimeMillis() / 1000));
//                     try {
//                         Thread.sleep(50);
//                     } catch (InterruptedException e) {
//                     }
//                 }
//             };
//             executor.submit(task1);
//             executor.submit(task2);
//             executor.submit(task3);
//             executor.shutdown();
//             while (!executor.isTerminated()) {
//                 try {
//                     Thread.sleep(10);
//                 } catch (InterruptedException e) {
//                 }
//             }
//         });
//         // Với TestStockRealtimePriceView, ta mong đợi:
//         // - Đối với RT1: log 1 lần khi giá là 100 và 1 lần khi chuyển sang 105
//         // - Đối với RT2: log 1 lần cho giá 200 và 1 lần cho giá 210
//         // - Đối với RT3: chỉ log 1 lần (với giá 300)
//         int occurrencesRT1_105 = countOccurrences(output, "[REALTIME] Realtime Price Update: RT1 is now $105.0");
//         int occurrencesRT2_200 = countOccurrences(output, "[REALTIME] Realtime Price Update: RT2 is now $200.0");
//         int occurrencesRT2_210 = countOccurrences(output, "[REALTIME] Realtime Price Update: RT2 is now $210.0");

//         if (occurrencesRT1_105 == 1 &&
//                 occurrencesRT2_200 == 1 && occurrencesRT2_210 == 1) {
//             System.out.println(
//                     ANSI_GREEN + "Pass FeederRealtime_Testcase_04: Multi-threaded Realtime Observer" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(
//                     ANSI_RED + "Fail FeederRealtime_Testcase_04: Multi-threaded Realtime Observer" + ANSI_RESET);
//         }
//     }

//     // Test 1: Chỉ sử dụng dữ liệu sàn HOSE
//     private static void Summary_Testcase_01() {
//         // Reset feeder
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             // Thêm các stock của HOSE (mã stock cần khớp với dữ liệu file JSON)
//             feeder.addStock(new Stock("VIC", "VIC"));
//             feeder.addStock(new Stock("VNM", "VNM"));
//             feeder.addStock(new Stock("HPG", "HPG"));
//             feeder.addStock(new Stock("MWG", "MWG"));
//             feeder.addStock(new Stock("VCB", "VCB"));

//             // Đăng ký viewer: realtime và ticker cho một số stock
//             feeder.registerViewer("VIC", new StockRealtimePriceView());
//             tickerView = new StockTickerView();
//             feeder.registerViewer("VIC", tickerView);
//             feeder.registerViewer("VIC", new StockAlertView(80000, 50000));
//             feeder.registerViewer("VNM", new StockRealtimePriceView());
//             StockTickerView tickerView = new StockTickerView();
//             feeder.registerViewer("VNM", tickerView);
//             feeder.registerViewer("VNM", new StockAlertView(120000, 80000));

//             for (int i = 3; i < 8; i++) {
//                 StockPrice sp = new StockPrice("VIC", 10000 * i + 15, 1000, System.currentTimeMillis() / 1000);
//                 StockPrice sp2 = new StockPrice("VNM", 15000 * i + 20, 1000, System.currentTimeMillis() / 1000);
//                 feeder.notify(sp);
//                 feeder.notify(sp2);
//             }
//         });

//         String expected = "[ALERT] VIC price changed significantly to $30015.0\n" +
//                 "[ALERT] VNM price changed significantly to $45020.0\n" +
//                 "[REALTIME] Realtime Price Update: VIC is now $40015.0\n" +
//                 "[ALERT] VIC price changed significantly to $40015.0\n" +
//                 "[REALTIME] Realtime Price Update: VNM is now $60020.0\n" +
//                 "[ALERT] VNM price changed significantly to $60020.0\n" +
//                 "[REALTIME] Realtime Price Update: VIC is now $50015.0\n" +
//                 "[REALTIME] Realtime Price Update: VNM is now $75020.0\n" +
//                 "[ALERT] VNM price changed significantly to $75020.0\n" +
//                 "[REALTIME] Realtime Price Update: VIC is now $60015.0\n" +
//                 "[REALTIME] Realtime Price Update: VNM is now $90020.0\n" +
//                 "[REALTIME] Realtime Price Update: VIC is now $70015.0\n" +
//                 "[REALTIME] Realtime Price Update: VNM is now $105020.0\n";

//         compareOutput("Summary_Testcase_01", expected, output);
//     }

//     // Test 2: RealTime Only (không có Alert)
//     private static void Summary_Testcase_02() {
//         // Reset feeder
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             // Thêm stock "ABC" vào feeder
//             feeder.addStock(new Stock("ABC", "Stock ABC"));

//             // Đăng ký viewer: realtime và ticker cho stock "ABC"
//             feeder.registerViewer("ABC", new StockRealtimePriceView());
//             tickerView = new StockTickerView();
//             feeder.registerViewer("ABC", tickerView);

//             // Notify 3 lần với các giá 100, 200, 300
//             for (int i = 1; i <= 3; i++) {
//                 StockPrice sp = new StockPrice("ABC", 100 * i, 1000, 0);
//                 feeder.notify(sp);
//                 long startTime = System.currentTimeMillis(); // Lấy thời gian bắt đầu
//                 while (System.currentTimeMillis() - startTime < 10500) {
//                     // Chờ 10 giây mà không dùng Thread.sleep()
//                 }
//             }
//         });

//         if (// output.contains("[REALTIME] Realtime Price Update: ABC is now $100.0") &&
//             // output.contains("[REALTIME] Realtime Price Update: ABC is now $200.0") &&
//             // output.contains("[REALTIME] Realtime Price Update: ABC is now $300.0") &&
//         output.contains(
//                 "[TICKER] Stock: ABC   | High:   100.00 | Low:   100.00 | Open:   100.00 | Close:   100.00 | Volume:     1000 | Avg Price:   100.00 | Timestamp: ")
//                 &&
//                 output.contains(
//                         "[TICKER] Stock: ABC   | High:   200.00 | Low:   100.00 | Open:   100.00 | Close:   200.00 | Volume:     1000 | Avg Price:   150.00 | Timestamp: ")
//                 &&
//                 output.contains(
//                         "[TICKER] Stock: ABC   | High:   300.00 | Low:   100.00 | Open:   100.00 | Close:   300.00 | Volume:     1000 | Avg Price:   200.00 | Timestamp: ")) {
//             System.out.println(ANSI_GREEN + "Pass Summary_Testcase_02: RealTime Only" + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(output);
//             System.out.println(ANSI_RED + "Fail Summary_Testcase_02: RealTime Only" + ANSI_RESET);
//         }
//     }

//     // Test 3: Viewer đăng ký cho stock không tồn tại
//     private static void Summary_Testcase_03() {
//         // Reset feeder
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             // Không thêm stock nào cho mã "NOTEXIST"
//             // Cố gắng đăng ký viewer cho stock "NOTEXIST"
//             feeder.registerViewer("NOTEXIST", new StockRealtimePriceView());
//         });

//         String expected = "[WARNING] Error when registering with NOTEXIST\n";
//         compareOutput("Summary_Testcase_03", expected, output);
//     }

//     // Test 4: Cập nhật giá xen kẽ cho stock "ABC" với realtime và alert
//     private static void Summary_Testcase_04() {
//         // Reset feeder
//         resetStockFeeder();
//         String output = captureOutput(() -> {
//             StockFeeder feeder = StockFeeder.getInstance();
//             // Thêm stock "ABC" vào feeder
//             feeder.addStock(new Stock("ABC", "Stock ABC"));
//             // Đăng ký realtime và alert cho "ABC"
//             feeder.registerViewer("ABC", new StockRealtimePriceView());
//             feeder.registerViewer("ABC", new StockAlertView(120, 80)); // Ngưỡng: alert nếu giá >= 120 hoặc <= 80

//             // Notify lần 1: Giá 100 (không vượt ngưỡng, nhưng realtime log xuất hiện và
//             // Alert log xuất hiện vì giá mới lần đầu)
//             feeder.notify(new StockPrice("ABC", 100, 1000, System.currentTimeMillis() / 1000));
//             // Notify lần 2: Giá 100 (không thay đổi, chỉ realtime log duy nhất từ lần đầu)
//             feeder.notify(new StockPrice("ABC", 100, 1000, System.currentTimeMillis() / 1000));
//             // Notify lần 3: Giá 150 (thay đổi, vượt ngưỡng cao, realtime và alert log)
//             feeder.notify(new StockPrice("ABC", 150, 1000, System.currentTimeMillis() / 1000));
//             // Notify lần 4: Giá 150 (không thay đổi, chỉ realtime log duy nhất từ lần cập
//             // nhật mới)
//             feeder.notify(new StockPrice("ABC", 150, 1000, System.currentTimeMillis() / 1000));
//         });
//         String expected = "[REALTIME] Realtime Price Update: ABC is now $150.0\n" +
//                 "[ALERT] ABC price changed significantly to $150.0\n";
//         compareOutput("Summary_Testcase_04", expected, output);
//     }

//     // Hàm hỗ trợ đếm số lần xuất hiện của text trong chuỗi
//     private static int countOccurrences(String str, String text) {
//         int count = 0, index = 0;
//         while ((index = str.indexOf(text, index)) != -1) {
//             count++;
//             index += text.length();
//         }
//         return count;
//     }

//     private static void printResult() {
//         if (count_pass_test == num_test) {
//             System.out.println(ANSI_GREEN + "Pass all test cases" + ANSI_RESET);
//         } else {
//             System.out.println(ANSI_RED + "Fail test cases" + ANSI_RESET);
//             System.out.println("Percent pass: " + (count_pass_test * 100) / num_test + "%");
//         }
//     }

//     private static String normalize(String s) {
//         // Loại bỏ tất cả khoảng trắng (space, tab, newline, carriage return)
//         return s.replaceAll("\\s+", "");
//     }

//     private static void compareOutput(String nameTest, String expected, String output) {
//         if (normalize(expected).equals(normalize(output))) {
//             System.out.println(ANSI_GREEN + nameTest + ANSI_RESET);
//             count_pass_test++;
//         } else {
//             System.out.println(ANSI_RED + nameTest + ANSI_RESET);
//             System.out.println("Expected:\n" + expected);
//             System.out.println("Output:\n" + output);
//         }
//     }

//     // Hàm hỗ trợ: chạy 1 đoạn code và bắt output của System.out
//     private static String captureOutput(Runnable testCode) {
//         // Lưu lại System.out ban đầu
//         PrintStream originalOut = System.out;
//         ByteArrayOutputStream baos = new ByteArrayOutputStream();
//         PrintStream ps = new PrintStream(baos);
//         System.setOut(ps);

//         // Chạy đoạn code cần test
//         testCode.run();

//         // Khôi phục System.out
//         System.out.flush();
//         System.setOut(originalOut);

//         return baos.toString();
//     }

//     // Sử dụng reflection để lấy kích thước của danh sách stockList trong
//     // StockFeeder
//     private static int getStockListSize(StockFeeder feeder) {
//         try {
//             Field field = StockFeeder.class.getDeclaredField("stockList");
//             field.setAccessible(true);
//             Object obj = field.get(feeder);
//             if (obj instanceof java.util.List) {
//                 return ((java.util.List<?>) obj).size();
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         return -1;
//     }

//     private static void resetStockFeeder() {
//         try {
//             Field instanceField = StockFeeder.class.getDeclaredField("instance");
//             instanceField.setAccessible(true);
//             instanceField.set(null, null); // Reset instance về null
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }


