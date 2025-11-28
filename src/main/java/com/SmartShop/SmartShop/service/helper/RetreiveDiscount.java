package com.SmartShop.SmartShop.service.helper;


import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.PromoCode;
import com.SmartShop.SmartShop.entity.enums.CustomerTier;
import com.SmartShop.SmartShop.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RetreiveDiscount {

    private final PromoCodeRepository promoCodeRepository;

    public double getClientDiscount(CustomerTier tier, double sousTotal) {
        if (tier == null) return 0;

        switch (tier) {
            case SILVER:
                return sousTotal >= 500 ? sousTotal * 0.05 : 0;
            case GOLD:
                return sousTotal >= 800 ? sousTotal * 0.10 : 0;
            case PLATINUM:
                return sousTotal >= 1200 ? sousTotal * 0.15 : 0;
            default:
                return 0;
        }
    }


    public double getPromoDiscount(String code, double total) {
        if (code == null || code.isBlank()) return 0;

        Optional<PromoCode> optionalPromo = promoCodeRepository.findByCode(code);
        if (optionalPromo.isEmpty()) return 0;

        PromoCode promo = optionalPromo.get();

        if (promo.isAlreadyUsed()) return 0;
        if (promo.getDateExpiration() != null && promo.getDateExpiration().isBefore(LocalDate.now()))
            return 0;

        return total * promo.getDiscountPercentage() / 100.0;
    }


}
