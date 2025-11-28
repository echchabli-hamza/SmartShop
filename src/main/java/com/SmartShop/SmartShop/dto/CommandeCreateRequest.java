package com.SmartShop.SmartShop.dto;

import com.SmartShop.SmartShop.entity.Client;
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
    private Double remise;
    private Double tva;


    private String promoCode;

    private List<OrderItemRequest> items;





}

