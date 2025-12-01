package com.SmartShop.SmartShop.service;


import com.SmartShop.SmartShop.entity.PromoCode;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }

    public PromoCode getPromoCodeById(Long id) {
        return promoCodeRepository.findById(id)
                .orElseThrow(() ->  new ResourceNotFoundException("PromoCode not found: " + id ));
    }

    @Transactional
    public PromoCode createPromoCode(PromoCode promoCode) {
        promoCode.setDateCreation(LocalDate.now());
        promoCode.setAlreadyUsed(false);
        return promoCodeRepository.save(promoCode);
    }

    @Transactional
    public PromoCode updatePromoCode(Long id, PromoCode updated) {
        PromoCode existing = getPromoCodeById(id);

        existing.setDiscountPercentage(updated.getDiscountPercentage());
        existing.setDateExpiration(updated.getDateExpiration());

        return promoCodeRepository.save(existing);
    }

    @Transactional
    public void deletePromoCode(Long id) {
        promoCodeRepository.deleteById(id);
    }


}
