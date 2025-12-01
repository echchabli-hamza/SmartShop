package com.SmartShop.SmartShop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Unit price is required")
    @Min(value = 1, message = "Unit price must be at least 1")
    private Double montant;
    private String typePaiement;
    @NotNull(message = "Unit price is required")

    private Integer commandeId ;
    private String statut;
    private String reference;
    private String banque;

}
