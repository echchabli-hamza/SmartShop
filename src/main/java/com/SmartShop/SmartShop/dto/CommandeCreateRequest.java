package com.SmartShop.SmartShop.dto;

import com.SmartShop.SmartShop.entity.Client;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandeCreateRequest {

    @NotNull(message = "Client ID is required")
    private Long clientId;

    @NotNull(message = "TVA is required")
    @Min(value = 5, message = "TVA must be at least 5")
    private Double tva;
    private String statut;
    private LocalDateTime date;
    private Double remise;



    private String promoCode;

    private List<OrderItemRequest> items;





}

