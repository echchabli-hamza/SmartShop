package com.SmartShop.SmartShop.service.strategy.impl;



import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

public class CheckPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.EN_ATTENTE);
        // Check-specific validation: bank, number, date
    }

    @Override
    public void encaisserPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.ENCAISSE);
        // Update date encaissement
    }

    @Override
    public void rejectPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.REJETE);
    }
}
