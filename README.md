# 🧪 Spring Boot Selenium WebDriver Demo

Dự án này là một ví dụ minh họa cách kết hợp **Spring Boot**, **Thymeleaf**, và **Selenium WebDriver** để xây dựng ứng dụng web và tự động hóa kiểm thử giao diện (E2E Testing) cũng như kiểm thử bảo mật cơ bản. Dự án cũng tích hợp **Allure Framework** để tạo báo cáo kiểm thử trực quan.

## 🌟 Tính năng nổi bật

1. **Giao diện đăng nhập hiện đại (Glassmorphism)**: Xây dựng bằng HTML/CSS và Thymeleaf.
2. **Page Object Model (POM)**: Cấu trúc test code chuẩn mực, dễ bảo trì, tái sử dụng (ví dụ: `DemoLoginPage`, `UtcLoginPage`).
3. **Kiểm thử tự động toàn diện**:
    - **E2E Test**: Kiểm tra luồng đăng nhập đúng/sai, chuyển hướng trang (`DemoLoginE2ETest`).
    - **Kiểm thử trang web bên ngoài**: Tương tác với trang web thực tế (`UtcLoginE2ETest` với cổng thông tin UTC).
    - **Security Test**: Kiểm tra khả năng chịu lỗi với các payload SQL Injection, XSS, và dữ liệu biên (`DemoLoginSecurityTest`).
4. **Allure Report**: Tự động chụp màn hình và lưu mã nguồn HTML của trang khi test case bị fail (rất hữu ích cho việc debug).
5. **Spring Boot Test**: Tự động khởi chạy server ở một port ngẫu nhiên (`RANDOM_PORT`) để Selenium có thể kiểm thử ứng dụng trực tiếp mà không cần bạn phải khởi động server thủ công trước.

## 🚀 Hướng dẫn cài đặt và chạy

### 1. Yêu cầu hệ thống
* **Java 21** trở lên.
* **Maven** (có thể dùng file `mvnw` đi kèm trong source code).
* Trình duyệt **Google Chrome** (dự án đã dùng `WebDriverManager` nên bạn không cần tự tải ChromeDriver, hệ thống sẽ tự lo).

### 2. Clone dự án về máy

Mở Terminal (hoặc Command Prompt / PowerShell) và chạy lệnh sau:

```bash
git clone <URL_CỦA_REPO>
cd Selenium-test
```
*(Lưu ý: Thay `<URL_CỦA_REPO>` bằng đường dẫn Git của dự án này, hoặc nếu bạn đã có sẵn thư mục code thì bỏ qua bước này)*

### 3. Chạy ứng dụng web (Tùy chọn)

Nếu bạn chỉ muốn bật server lên để tự tay xem và test thử giao diện trang đăng nhập demo:

```bash
mvn spring-boot:run
```
Sau khi server chạy lên, hãy mở trình duyệt và truy cập: [http://localhost:8080/login](http://localhost:8080/login)
* Tài khoản hợp lệ: `admin`
* Mật khẩu hợp lệ: `123456`

### 4. Chạy bộ kiểm thử tự động (Selenium Tests)

Để thực thi tất cả các kịch bản kiểm thử (E2E & Security) bằng Selenium:

```bash
mvn clean test
```

*Trong quá trình chạy, bạn sẽ thấy trình duyệt Chrome tự động mở lên, thực hiện các thao tác (điền text, click nút) cực kỳ nhanh chóng rồi tự động đóng lại.*

### 5. Xem Báo cáo Kiểm thử (Allure Report)

Dự án đã tích hợp plugin Allure Maven để tạo báo cáo chi tiết. Sau khi chạy lệnh test xong, hãy xuất và xem báo cáo bằng lệnh:

```bash
mvn allure:serve
```

Lệnh này sẽ xử lý kết quả test, khởi động một server nhỏ và tự động mở trình duyệt hiển thị giao diện Dashboard báo cáo của Allure.

## 📂 Cấu trúc thư mục Test quan trọng

Toàn bộ code test nằm trong `src/test/java/com/example/seleniumtest/`:

* `base/BaseTest.java`: Lớp cấu hình WebDriver chung (khởi tạo Chrome, cấu hình timeout). Mọi class test đều kế thừa lớp này.
* `pages/`: Chứa các lớp Page Object (đại diện cho cấu trúc của các trang web).
    * `BasePage.java`: Các hàm tương tác tiện ích (click, type, wait).
    * `DemoLoginPage.java`: Lớp tương tác với trang login nội bộ của dự án.
    * `UtcLoginPage.java`: Lớp tương tác với trang login của hệ thống bên ngoài.
* `support/AllureScreenshotExtension.java`: Tiện ích mở rộng của JUnit 5 giúp tự động chụp màn hình browser khi một test case bị đánh dấu là FAILED.
* `tests/`: Chứa các kịch bản kiểm thử thực tế.

## 🛠️ Công nghệ sử dụng
* **Spring Boot 4.1.1** (Web, Thymeleaf, Test)
* **Selenium WebDriver 4.27.0**
* **WebDriverManager 5.9.2**: Quản lý driver tự động.
* **JUnit 5**
* **Allure Report 2.30.0**
