package com.SmartShop.SmartShop.service.strategy.impl;

import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

import java.time.LocalDate;

public class CheckPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {


        if (paiement.getMontant() <= 0) {
            throw new RuntimeException("Cheque amount must be greater than zero.");
        }


        if (paiement.getBanque() == null || paiement.getBanque().isEmpty()) {
            throw new RuntimeException("Bank name is required for cheque payment.");
        }
        if (paiement.getReference() == null || paiement.getReference().isEmpty()) {
            throw new RuntimeException("Cheque number/reference is required.");
        }

        paiement.setStatut(PaymentStatus.EN_ATTENTE);
    }

    @Override
    public void encaisserPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.ENCAISSE);
        paiement.setDateEncaissement(LocalDate.now());
    }

    @Override
    public void rejectPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.REJETE);
    }
}
