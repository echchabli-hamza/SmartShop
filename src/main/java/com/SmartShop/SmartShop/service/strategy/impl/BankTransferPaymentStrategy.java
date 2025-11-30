package com.SmartShop.SmartShop.service.strategy.impl;




import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

public class BankTransferPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.EN_ATTENTE);
        // Transfer-specific rules: bank, reference, date
    }

    @Override
    public void encaisserPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.ENCAISSE);
    }

    @Override
    public void rejectPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.REJETE);
    }
}
