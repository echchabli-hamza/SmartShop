package com.SmartShop.SmartShop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemDTO {
    private Long id;
    private Long produitId;
    private String produitNom;
    private Integer quantite;
    private Double prixUnitaire;
    private Double totalLigne;
}
