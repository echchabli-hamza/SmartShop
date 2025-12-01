package com.SmartShop.SmartShop.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String nom;

    @NotNull(message = "Unit price is required")
    @Min(value = 1, message = "Unit price must be at least 1")
    private Double prixUnitaire;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 1, message = "Stock must be at least 1")
    private Integer stockDisponible;
}
