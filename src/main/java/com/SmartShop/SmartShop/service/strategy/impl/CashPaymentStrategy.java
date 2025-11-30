package com.SmartShop.SmartShop.service.strategy.impl;




import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {
        // Cash payment rules
        if (paiement.getMontant() > 20000) {
            throw new RuntimeException("Cash payment exceeds legal limit!");
        }
        paiement.setStatut(PaymentStatus.EN_ATTENTE);
        // Additional logic if needed
    }

    @Override
    public void encaisserPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.ENCAISSE);
        // Update payment date, history, etc.
    }

    @Override
    public void rejectPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.REJETE);
    }
}
