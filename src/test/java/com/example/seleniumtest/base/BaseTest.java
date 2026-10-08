package com.example.seleniumtest.base;

import com.example.seleniumtest.support.AllureScreenshotExtension;
import com.example.seleniumtest.support.DriverHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Lớp cha cho mọi test E2E: khởi tạo WebDriver một lần cho cả class,
 * cấu hình timeout, và đóng driver khi class chạy xong.
 * Mọi test class chỉ cần `extends BaseTest` để có sẵn `driver`.
 */
@ExtendWith(AllureScreenshotExtension.class)
public abstract class BaseTest {

    protected static WebDriver driver;

    @BeforeAll
    static void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--disable-notifications");
        // options.addArguments("--headless=new");

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