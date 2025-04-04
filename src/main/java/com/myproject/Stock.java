package com.myproject;

public class Stock {
    private String code;
    private String name;

    public Stock(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "Stock{" + "code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}
