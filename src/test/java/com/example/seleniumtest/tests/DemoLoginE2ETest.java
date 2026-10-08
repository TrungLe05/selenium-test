package com.example.seleniumtest.tests;

import com.example.seleniumtest.base.BaseTest;
import com.example.seleniumtest.pages.DemoLoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Epic("Xác thực")
@Feature("Đăng nhập — Demo app")
class DemoLoginE2ETest extends BaseTest {

    @LocalServerPort
    private int port;

    private DemoLoginPage loginPage;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    void openLoginPage() {
        loginPage = new DemoLoginPage(driver).open(baseUrl());
    }

    @Test
    @DisplayName("TC-01 | Trang đăng nhập hiển thị đủ thành phần")
    @Severity(SeverityLevel.CRITICAL)
    void loginPageShouldLoad() {
        assertTrue(loginPage.isLoaded());
    }

    @Test
    @DisplayName("TC-02 | Sai mật khẩu → hiển thị lỗi")
    @Severity(SeverityLevel.NORMAL)
    void wrongCredentialsShouldShowError() {
        loginPage.login("wrong_user", "wrong_pass");

        assertTrue(loginPage.isErrorShown());
        assertTrue(loginPage.errorText().contains("không đúng"));
        assertTrue(loginPage.currentUrl().contains("/login"));
    }

    @Test
    @DisplayName("TC-03 | Đúng tài khoản demo → đăng nhập thành công")
    @Severity(SeverityLevel.BLOCKER)
    void validCredentialsShouldLogin() {
        loginPage.login("admin", "123456");

        assertTrue(loginPage.currentUrl().contains("/dashboard"));
    }
}