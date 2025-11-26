package com.SmartShop.SmartShop.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeCreateRequest {
    private Long clientId;
    private String statut;
    private LocalDateTime date;
    private Double sousTotal;
    private Double remise;
    private Double tva;
    private Double total;
    private Double montantRestant;

    // Only promo code string on create
    private String promoCode;

    private List<OrderItemDTO> items;
}

