package com.SmartShop.SmartShop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDTO {
    private Long id;
    private String code;
    private Integer discountPercentage;
    private LocalDate dateCreation;
    private LocalDate dateExpiration;
    private boolean singleUse;
    private boolean alreadyUsed;
}

