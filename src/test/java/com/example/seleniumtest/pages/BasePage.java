package com.example.seleniumtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Các thao tác chung mà mọi Page Object đều cần: chờ phần tử, click, gõ chữ.
 * Các lớp Page cụ thể kế thừa lớp này thay vì gọi trực tiếp WebDriverWait
 * trong từng test, giúp test ngắn gọn và dễ đọc hơn.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void type(By locator, String text) {
        WebElement el = waitClickable(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void click(By locator) {
        waitClickable(locator).click();
    }

    protected boolean isDisplayed(By locator) {
        return !driver.findElements(locator).isEmpty() && driver.findElement(locator).isDisplayed();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String pageSource() {
        return driver.getPageSource();
    }
}