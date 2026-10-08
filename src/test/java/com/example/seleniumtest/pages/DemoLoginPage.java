package com.example.seleniumtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DemoLoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON   = By.id("btn-login");
    private static final By ERROR_ALERT    = By.cssSelector(".alert-error");

    public DemoLoginPage(WebDriver driver) {
        super(driver);
    }

    public DemoLoginPage open(String baseUrl) {
        driver.get(baseUrl + "/login");
        return this;
    }

    public DemoLoginPage typeUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    public DemoLoginPage typePassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public void submit() {
        click(LOGIN_BUTTON);
    }

    public DemoLoginPage login(String username, String password) {
        typeUsername(username);
        typePassword(password);
        submit();
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(USERNAME_INPUT) && isDisplayed(PASSWORD_INPUT) && isDisplayed(LOGIN_BUTTON);
    }

    public boolean isErrorShown() {
        return isDisplayed(ERROR_ALERT);
    }

    public String errorText() {
        return waitVisible(ERROR_ALERT).getText();
    }
}