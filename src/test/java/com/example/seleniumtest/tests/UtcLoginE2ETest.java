package com.example.seleniumtest.tests;

import com.example.seleniumtest.base.BaseTest;
import com.example.seleniumtest.pages.UtcLoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test UI / trường hợp thất bại cho trang đăng nhập UTC.
 * Không test đăng nhập thành công (không có tài khoản test),
 * không test SQL injection / XSS lên hệ thống này (không được phép).
 */
@Epic("Xác thực")
@Feature("Đăng nhập văn phòng điện tử UTC")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UtcLoginE2ETest extends BaseTest {

    private UtcLoginPage loginPage;

    @BeforeEach
    void openLoginPage() {
        loginPage = new UtcLoginPage(driver).open();
    }

    @Test
    @Order(1)
    @DisplayName("TC-01 | Trang đăng nhập hiển thị đủ thành phần")
    @Severity(SeverityLevel.CRITICAL)
    void loginPageShouldLoad() {
        assertTrue(loginPage.currentUrl().contains("/Login"));
        assertTrue(loginPage.isLoaded());
        assertTrue(loginPage.isRememberLabelDisplayed());
        assertTrue(loginPage.isEmailLoginLinkDisplayed());
    }

    @Test
    @Order(2)
    @DisplayName("TC-02 | Click label 'Giữ tôi luôn đăng nhập' sẽ tick/bỏ tick")
    @Severity(SeverityLevel.MINOR)
    void rememberCheckboxShouldToggle() {
        assertFalse(loginPage.isRememberChecked());
        loginPage.toggleRemember();
        assertTrue(loginPage.isRememberChecked());
        loginPage.toggleRemember();
        assertFalse(loginPage.isRememberChecked());
    }

    @Test
    @Order(3)
    @DisplayName("TC-03 | Link email login trỏ về Google OAuth")
    @Severity(SeverityLevel.NORMAL)
    void emailLoginLinkShouldPointToGoogle() {
        String href = loginPage.emailLoginHref();
        assertNotNull(href);
        assertTrue(href.startsWith("https://accounts.google.com/"));
    }

    @Test
    @Order(4)
    @DisplayName("TC-04 | Để trống thông tin → vẫn ở trang Login")
    @Severity(SeverityLevel.NORMAL)
    void emptyCredentialsShouldNotLogin() {
        loginPage.submit();
        assertTrue(loginPage.currentUrl().contains("/Login"));
    }

    @Test
    @Order(5)
    @DisplayName("TC-05 | Chỉ nhập username, bỏ trống password → vẫn ở trang Login")
    @Severity(SeverityLevel.NORMAL)
    void missingPasswordShouldNotLogin() {
        loginPage.typeUsername("test_user_only");
        loginPage.submit();
        assertTrue(loginPage.currentUrl().contains("/Login"));
    }
}