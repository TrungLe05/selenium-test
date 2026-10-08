package com.example.seleniumtest.tests;

import com.example.seleniumtest.base.BaseTest;
import com.example.seleniumtest.pages.DemoLoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.NoAlertPresentException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Security test cho app Thymeleaf CỦA CHÍNH BẠN. App hiện dùng String.equals()
 * (không có query SQL thật), nên các case SQLi ở đây kiểm tra tính chịu lỗi
 * (robustness) chứ không khai thác được injection thật.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Epic("Bảo mật")
@Feature("Đăng nhập — input độc hại")
class DemoLoginSecurityTest extends BaseTest {

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

    static Stream<String> sqlInjectionPayloads() {
        return Stream.of(
                "' OR '1'='1",
                "' OR 1=1 --",
                "admin'--",
                "'; DROP TABLE users; --",
                "' UNION SELECT 1,1,1 --"
        );
    }

    @ParameterizedTest(name = "TC-SQLI-{index} | payload = {0}")
    @MethodSource("sqlInjectionPayloads")
    @DisplayName("Input dạng SQL injection không được bypass đăng nhập")
    @Story("SQL injection")
    @Severity(SeverityLevel.CRITICAL)
    void sqlInjectionLikeInputShouldNotBypassLogin(String payload) {
        loginPage.login(payload, "bat_ky_mat_khau");

        assertTrue(loginPage.currentUrl().contains("/login"),
                "Payload '" + payload + "' không được phép bypass đăng nhập");
        assertFalse(loginPage.pageSource().toLowerCase().contains("internal server error"));
        assertTrue(loginPage.isErrorShown());
    }

    static Stream<String> xssPayloads() {
        return Stream.of(
                "<script>alert(1)</script>",
                "<img src=x onerror=alert(1)>",
                "\"><script>alert(1)</script>"
        );
    }

    @ParameterizedTest(name = "TC-XSS-{index} | payload = {0}")
    @MethodSource("xssPayloads")
    @DisplayName("Input chứa script không được thực thi")
    @Story("XSS phản chiếu")
    @Severity(SeverityLevel.CRITICAL)
    void xssPayloadShouldNotExecute(String payload) {
        loginPage.login(payload, "bat_ky_mat_khau");

        assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert(),
                "Nếu không ném exception nghĩa là alert() đã thực thi — XSS thành công, đây là lỗi nghiêm trọng!");
    }

    static Stream<String> edgeCaseInputs() {
        return Stream.of(
                "   ",
                "\t\n",
                "田中太郎",
                "' OR ''='",
                "a".repeat(500)
        );
    }

    @ParameterizedTest(name = "TC-EDGE-{index}")
    @MethodSource("edgeCaseInputs")
    @DisplayName("Input biên không làm app crash")
    @Story("Input biên")
    @Severity(SeverityLevel.NORMAL)
    void edgeCaseInputShouldNotCrashApp(String input) {
        loginPage.login(input, "123456");

        assertFalse(loginPage.pageSource().isBlank());
        assertFalse(loginPage.pageSource().toLowerCase().contains("internal server error"));
    }
}