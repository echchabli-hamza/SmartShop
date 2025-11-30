package com.SmartShop.SmartShop.service.strategy.impl;

import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

import java.time.LocalDate;

public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {



        if (paiement.getMontant() > 20000) {
            throw new RuntimeException("Cash payment exceeds legal limit!");
        }

        paiement.setReference(null);
        paiement.setBanque(null);

        paiement.setStatut(PaymentStatus.EN_ATTENTE);

        paiement.setDatePaiement(LocalDate.now());
    }

    @Override
    public void encaisserPayment(Paiement paiement) {
        paiement.setStatut(PaymentStatus.ENCAISSE);

        paiement.setDateEncaissement(LocalDate.now());
    }

    @Override
    public void rejectPayment(Paiement paiement) {


        paiement.setStatut(PaymentStatus.REJETE);

        paiement.setDateEncaissement(null);
    }
}
