package com.example.seleniumtest.controller;

import com.example.seleniumtest.dto.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    // Tài khoản giả lập (demo) — không có DB thật
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "123456";

    /**
     * GET /login — hiển thị trang login
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login"; // → templates/login.html
    }

    /**
     * POST /login — xử lý đăng nhập
     * Nếu đúng → redirect sang /dashboard
     * Nếu sai  → quay lại login với thông báo lỗi
     */
    @PostMapping("/login")
    public String handleLogin(
            @ModelAttribute LoginRequest loginRequest,
            Model model
    ) {
        if (DEMO_USERNAME.equals(loginRequest.getUsername())
                && DEMO_PASSWORD.equals(loginRequest.getPassword())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("loginRequest", loginRequest);
        model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
        return "login";
    }

    /**
     * GET /dashboard — trang đích sau khi đăng nhập thành công
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("username", DEMO_USERNAME);
        return "dashboard";
    }
}
