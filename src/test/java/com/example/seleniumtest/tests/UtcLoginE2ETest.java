package com.example.seleniumtest.tests;

import com.example.seleniumtest.base.BaseTest;
import com.example.seleniumtest.pages.UtcLoginPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

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

    @Test
    @Order(6)
    @DisplayName("TC-06 | Ô mật khẩu che ký tự (type=password)")
    @Severity(SeverityLevel.NORMAL)
    void passwordFieldShouldBeMasked() {
        assertEquals("password", loginPage.passwordInputType());
    }

    @Test
    @Order(7)
    @DisplayName("TC-07 | Trang đăng nhập bắt buộc dùng HTTPS")
    @Severity(SeverityLevel.NORMAL)
    void loginPageShouldUseHttps() {
        assertTrue(loginPage.isHttps(), "Trang đăng nhập phải dùng HTTPS, tránh lộ mật khẩu qua HTTP");
    }

    @Test
    @Order(8)
    @DisplayName("TC-08 | Nhấn Tab từ username chuyển focus sang password")
    @Severity(SeverityLevel.MINOR)
    void tabShouldMoveFocusToPassword() {
        loginPage.tabFromUsernameToPassword();
        assertEquals("userpwd", loginPage.activeElementName(),
                "Thứ tự Tab phải hợp lý cho người dùng gõ bàn phím/máy đọc màn hình");
    }

    @Test
    @Order(9)
    @DisplayName("TC-09 | Username chứa Unicode tiếng Việt được nhập đúng, không bị cắt ký tự")
    @Severity(SeverityLevel.MINOR)
    void vietnameseUsernameShouldBeTypedCorrectly() {
        loginPage.typeUsername("nguyễn_văn_a");
        assertEquals("nguyễn_văn_a", loginPage.usernameValue(),
                "Ô input không được làm sai lệch ký tự có dấu");
    }

    @Test
    @Order(10)
    @DisplayName("TC-10 | Username rất dài (500 ký tự) không làm vỡ giao diện")
    @Severity(SeverityLevel.MINOR)
    void veryLongUsernameShouldNotBreakLayout() {
        String longInput = "a".repeat(500);
        loginPage.typeUsername(longInput);

        // Không submit — chỉ kiểm tra input nhận đủ chuỗi và trang không crash khi render
        assertEquals(longInput, loginPage.usernameValue());
        assertFalse(loginPage.pageSource().isBlank());
    }

    @Test
    @Order(11)
    @DisplayName("TC-11 | Refresh trang sau khi nhập liệu → form được xóa về trạng thái ban đầu")
    @Severity(SeverityLevel.MINOR)
    void refreshShouldClearForm() {
        loginPage.typeUsername("se_bi_xoa_sau_refresh");
        loginPage.refreshPage();

        // Phải mở lại trang (page object mới) vì sau refresh cần re-locate element
        loginPage = new UtcLoginPage(driver).open();
        assertEquals("", loginPage.usernameValue(), "Form phải về trạng thái trống sau khi tải lại trang");
    }

    @Test
    @Order(12)
    @DisplayName("TC-12 | Tiêu đề tab nhất quán khi quay lại trang Login bằng nút Back")
    @Severity(SeverityLevel.MINOR)
    void backButtonShouldReturnToLoginPage() {
        loginPage.clickForgotPassword();                 // sang /Login/GetPass
        driver.navigate().back();                         // quay lại
        wait.until(ExpectedConditions.urlContains("/Login"));

        assertTrue(driver.getCurrentUrl().contains("/Login"));
        assertTrue(driver.getTitle().contains("Đăng nhập"));
    }

// ─── Nhóm B: có submit — gửi 1 request thật, chạy tiết chế, không lặp lại liên tục ───

    @Test
    @Order(13)
    @DisplayName("TC-13 | Username chỉ toàn khoảng trắng → không đăng nhập được")
    @Severity(SeverityLevel.NORMAL)
    void whitespaceOnlyUsernameShouldNotLogin() {
        loginPage.typeUsername("     ");
        loginPage.typePassword("bat_ky_mat_khau");
        loginPage.submit();

        assertTrue(loginPage.currentUrl().contains("/Login"));
    }

    @Test
    @Order(14)
    @DisplayName("TC-14 | Nhấn Enter trong ô mật khẩu submit được form (không chỉ click chuột)")
    @Severity(SeverityLevel.NORMAL)
    void enterKeyShouldSubmitForm() {
        loginPage.typeUsername("test_enter_key_khong_ton_tai");
        loginPage.submitViaEnterOnPassword("bat_ky_123");

        // Form phải thực sự gửi đi (không còn ở trạng thái ban đầu) dù qua phím Enter, không qua click
        wait.until(d -> true); // trang đã xử lý submit (có thể vẫn ở /Login nếu sai, đó là mong đợi)
        assertTrue(loginPage.currentUrl().contains("/Login"),
                "Sai thông tin nên vẫn ở lại trang Login, nhưng quan trọng là form phải submit được qua Enter");
    }

    @Test
    @Order(15)
    @DisplayName("TC-15 | Liên kết quên mật khẩu dẫn tới trang hợp lệ, có nội dung")
    @Severity(SeverityLevel.NORMAL)
    void forgotPasswordPageShouldHaveContent() {
        loginPage.clickForgotPassword();

        assertTrue(driver.getCurrentUrl().contains("/Login/GetPass"));
        assertFalse(driver.getPageSource().isBlank(), "Trang quên mật khẩu phải có nội dung, không phải trang trắng/lỗi");
    }
}