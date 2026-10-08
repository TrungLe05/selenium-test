package com.example.seleniumtest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium E2E test cho trang Login Thymeleaf.
 *
 * ┌─────────────────────────────────────────────────────────┐
 * │  Spring Boot khởi động server thật trên random port     │
 * │  Selenium mở Chrome → thao tác → kiểm tra kết quả      │
 * └─────────────────────────────────────────────────────────┘
 *
 * Chạy:  mvn test -Dtest=LoginSeleniumTest
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginSeleniumTest {

    /** Port được Spring Boot cấp phát lúc chạy test (tránh xung đột 8080) */
    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private WebDriverWait wait;

    // ─── URL helpers ──────────────────────────────────────────────────────────

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    // ─── Lifecycle ────────────────────────────────────────────────────────────

    @BeforeAll
    static void setupDriver() {
        // WebDriverManager tự tải chromedriver đúng phiên bản, không cần cài tay
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Bỏ comment dòng dưới nếu muốn chạy headless (không hiện browser)
        // options.addArguments("--headless=new");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @BeforeEach
    void setupWait() {
        // Explicit wait — thay thế Thread.sleep(), chờ element tối đa 10 giây
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit(); // đóng browser và giải phóng tài nguyên
        }
    }

    // ─── Test cases ───────────────────────────────────────────────────────────

    /**
     * TC-01: Trang /login load thành công, có đủ các phần tử form.
     */
    @Test
    @Order(1)
    @DisplayName("TC-01 | Trang /login hiển thị đúng")
    void loginPageShouldLoad() {
        driver.get(url("/login"));

        // Kiểm tra tiêu đề tab
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        assertTrue(title.contains("Đăng nhập"), "Tiêu đề trang phải chứa 'Đăng nhập'");

        // Kiểm tra input username có trên trang
        WebElement usernameInput = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("username"))
        );
        assertTrue(usernameInput.isDisplayed(), "Input username phải hiển thị");

        // Kiểm tra input password
        WebElement passwordInput = driver.findElement(By.id("password"));
        assertTrue(passwordInput.isDisplayed(), "Input password phải hiển thị");

        // Kiểm tra nút submit
        WebElement submitBtn = driver.findElement(By.id("btn-login"));
        assertTrue(submitBtn.isDisplayed(), "Nút đăng nhập phải hiển thị");

        System.out.println("TC-01 PASSED — trang login load đúng");
    }


}
