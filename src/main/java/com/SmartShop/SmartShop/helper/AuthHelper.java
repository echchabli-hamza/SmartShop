//package com.SmartShop.SmartShop.helper;
//
//
//import com.SmartShop.SmartShop.entity.User;
//import com.SmartShop.SmartShop.entity.enums.UserRole;
//import com.SmartShop.SmartShop.exception.customeExaptions.UnauthorizedException;
//import com.SmartShop.SmartShop.repository.UserRepository;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AuthHelper {
//
//    private final UserRepository userRepository;
//
//
//
//    public void checkAuthenticatedUserRole(HttpSession session, UserRole role) {
//        String username = (String) session.getAttribute("username");
//        if (username == null) {
//            throw new UnauthorizedException("No user logged in");
//        }
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UnauthorizedException("User not found"));
//
//        if (!user.getRole().equals(role)) {
//            throw new UnauthorizedException("Access denied for role: " + role);
//        }
//    }
//}
//
