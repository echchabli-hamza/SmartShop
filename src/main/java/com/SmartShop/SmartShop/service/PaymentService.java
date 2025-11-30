package com.SmartShop.SmartShop.service;

import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentType;
import com.SmartShop.SmartShop.repository.PaiementRepository;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;
import com.SmartShop.SmartShop.service.strategy.impl.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaiementRepository paiementRepository;
    private final PaymentStrategyFactory strategyFactory;

    @Transactional
    public Paiement addPayment(Paiement paiement) {
        PaymentStrategy strategy = strategyFactory.getStrategy(paiement.getTypePaiement());
        strategy.addPayment(paiement);
        return paiementRepository.save(paiement);
    }

    @Transactional
    public Paiement encaisserPayment(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        PaymentStrategy strategy = strategyFactory.getStrategy(paiement.getTypePaiement());
        strategy.encaisserPayment(paiement);
        return paiementRepository.save(paiement);
    }

    @Transactional
    public Paiement rejectPayment(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        PaymentStrategy strategy = strategyFactory.getStrategy(paiement.getTypePaiement());
        strategy.rejectPayment(paiement);
        return paiementRepository.save(paiement);
    }
}
