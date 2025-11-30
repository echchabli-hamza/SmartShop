package com.SmartShop.SmartShop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaiementDTO {
    private Long id;
    private Integer numeroPaiement;
    private Double montant;
    private String typePaiement;
    private Integer commandeId ;
    private String statut;
    private String reference;
    private String banque;

}
