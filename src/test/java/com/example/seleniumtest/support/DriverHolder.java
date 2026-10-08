package com.example.seleniumtest.support;


import org.openqa.selenium.WebDriver;

public class DriverHolder {
    private static volatile WebDriver driver;

    public static void set(WebDriver d) { driver = d; }
    public static WebDriver get() { return driver; }
}