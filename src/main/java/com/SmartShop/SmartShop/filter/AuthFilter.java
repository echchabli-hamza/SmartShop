package com.SmartShop.SmartShop.filter;

import com.SmartShop.SmartShop.entity.User;
import com.SmartShop.SmartShop.entity.enums.UserRole;
import com.SmartShop.SmartShop.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public AuthFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response); // allow access
            return;
        }

        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            sendError(request,response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: no user in session");
            return;
        }

        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            sendError(request,response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }


        request.setAttribute("authenticatedUser", user);


        if (path.startsWith("/api/admin/")) {
            if (user.getRole() != UserRole.ADMIN) {
                sendError( request, response, HttpServletResponse.SC_FORBIDDEN, "Forbidden: admin only");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }


        if (path.startsWith("/api/client/")) {
            if (user.getRole() != UserRole.CLIENT) {
                sendError( request, response, HttpServletResponse.SC_FORBIDDEN, "Forbidden: client only");
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }





        sendError( request,response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
    }

    private void sendError(HttpServletRequest request,HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        String json = "{"
                + "\"timestamp\":\"" + LocalDateTime.now() + "\","
                + "\"status\":" + status + ","
                + "\"message\":\"" + message + "\","
                + "\"path\":\"" + request.getRequestURI() + "\""
                + "}";

        response.getWriter().write(json);
    }
}
