package com.SmartShop.SmartShop.controller;




import com.SmartShop.SmartShop.dto.LoginRequestDto;
import com.SmartShop.SmartShop.dto.SessionResponse;
import com.SmartShop.SmartShop.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request , HttpSession session) {
        try {
            String username = authService.authenticate(
                    request.getUsername(),
                    request.getPassword()
            );


            session.setAttribute("username", username);



            return ResponseEntity.ok("ok");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");

        return ResponseEntity.ok(response);
    }

}
