package com.SmartShop.SmartShop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private String niveau; // BASIC / SILVER / GOLD / PLATINUM
    private Integer totalOrders;
    private Double totalSpent;
}
