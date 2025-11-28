package com.SmartShop.SmartShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeDTO {
    private Long id;
    private Long clientId;
    private String status; // PENDING / CONFIRMED / CANCELED / REJECTED
    private LocalDateTime date;
    private Double sousTotal;
    private Double remise;
    private Double tva;
    private Double total;
    private Double montantRestant;
    private PromoCodeDTO promoCode;
    private List<OrderItemDTO> items;
    private List<PaiementDTO> paiements;



}
