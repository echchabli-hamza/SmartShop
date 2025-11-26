package com.SmartShop.SmartShop.dto;

import com.SmartShop.SmartShop.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionResponse {
    private String username;
    private UserRole role;
}
