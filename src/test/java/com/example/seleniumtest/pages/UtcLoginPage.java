package com.example.seleniumtest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class UtcLoginPage extends BasePage {

    public static final String URL = "https://vanphongdientu.utc.edu.vn/Login";

    private static final By USERNAME_INPUT       = By.name("username");
    private static final By PASSWORD_INPUT       = By.name("userpwd");
    private static final By REMEMBER_CHECKBOX    = By.id("persistent");
    private static final By REMEMBER_LABEL       = By.cssSelector("label.check");
    private static final By LOGIN_BUTTON         = By.cssSelector("input.submit_login");
    private static final By EMAIL_LOGIN_LINK     = By.cssSelector("a.button[href*='accounts.google.com']");
    private static final By FORGOT_PASSWORD_LINK = By.cssSelector("div.helps a[href='/Login/GetPass']");

    public UtcLoginPage(WebDriver driver) {
        super(driver);
    }

    public UtcLoginPage open() {
        driver.manage().deleteAllCookies();
        driver.get(URL);
        return this;
    }

    public UtcLoginPage typeUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    public UtcLoginPage typePassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    public void submit() {
        click(LOGIN_BUTTON);
    }

    public boolean isLoaded() {
        return isDisplayed(USERNAME_INPUT) && isDisplayed(PASSWORD_INPUT) && isDisplayed(LOGIN_BUTTON);
    }

    public boolean isRememberLabelDisplayed() {
        return isDisplayed(REMEMBER_LABEL);
    }

    public boolean isRememberChecked() {
        return driver.findElement(REMEMBER_CHECKBOX).isSelected();
    }

    public void toggleRemember() {
        click(REMEMBER_LABEL);
    }

    public boolean isEmailLoginLinkDisplayed() {
        return isDisplayed(EMAIL_LOGIN_LINK);
    }

    public String emailLoginHref() {
        return driver.findElement(EMAIL_LOGIN_LINK).getDomAttribute("href");
    }

    public void clickForgotPassword() {
        click(FORGOT_PASSWORD_LINK);
        wait.until(ExpectedConditions.urlContains("/Login/GetPass"));
    }

    // Thêm vào UtcLoginPage, bên cạnh các method cũ

    public String usernameValue() {
        return driver.findElement(USERNAME_INPUT).getDomProperty("value");
    }

    public String passwordInputType() {
        return driver.findElement(PASSWORD_INPUT).getDomProperty("type");
    }

    public boolean isHttps() {
        return driver.getCurrentUrl().startsWith("https://");
    }

    public void tabFromUsernameToPassword() {
        driver.findElement(USERNAME_INPUT).sendKeys(org.openqa.selenium.Keys.TAB);
    }

    public String activeElementName() {
        return driver.switchTo().activeElement().getDomAttribute("name");
    }

    public void submitViaEnterOnPassword(String dummyPassword) {
        driver.findElement(PASSWORD_INPUT).sendKeys(dummyPassword + org.openqa.selenium.Keys.ENTER);
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}