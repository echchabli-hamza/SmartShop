package com.SmartShop.SmartShop.controller;

import com.SmartShop.SmartShop.entity.PromoCode;
import com.SmartShop.SmartShop.service.PromoCodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promo")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping
    public List<PromoCode> getAllPromoCodes() {
        return promoCodeService.getAllPromoCodes();
    }

    @PostMapping
    public PromoCode createPromoCode(@RequestBody PromoCode promoCode) {
        return promoCodeService.createPromoCode(promoCode);
    }

    @PutMapping("/{id}")
    public PromoCode updatePromoCode(@PathVariable Long id, @RequestBody PromoCode promoCode) {
        return promoCodeService.updatePromoCode(id, promoCode);
    }

    @DeleteMapping("/{id}")
    public void deletePromoCode(@PathVariable Long id) {
        promoCodeService.deletePromoCode(id);
    }


}
