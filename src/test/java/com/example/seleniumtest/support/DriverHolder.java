package com.example.seleniumtest.support;


public class DriverHolder {
    private static volatile WebDriver driver;

    public static void set(WebDriver d) { driver = d; }
    public static WebDriver get() { return driver; }
}