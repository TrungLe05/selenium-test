package com.example.seleniumtest.base;

import com.example.seleniumtest.support.AllureScreenshotExtension;
import com.example.seleniumtest.support.DriverHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@ExtendWith(AllureScreenshotExtension.class)
public abstract class BaseTest {

    protected static WebDriver driver;
    protected WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @BeforeAll
    static void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        DriverHolder.set(driver);
    }

    @AfterAll
    static void teardownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}