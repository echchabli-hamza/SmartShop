package com.SmartShop.SmartShop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String produitNom;
    private Integer quantite;
    private Double prixUnitaire;
    private Double totalLigne;




}
