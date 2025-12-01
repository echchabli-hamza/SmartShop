package com.SmartShop.SmartShop.service.strategy.impl;




import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;

public class BankTransferPaymentStrategy implements PaymentStrategy {

    @Override
    public void addPayment(Paiement paiement) {





        if (paiement.getBanque() == null || paiement.getBanque().isEmpty()) {
            throw new ResourceNotFoundException("Bank information is required for a bank transfer");
        }


        if (paiement.getReference() == null || paiement.getReference().isEmpty()) {
            throw new ResourceNotFoundException("Bank transfer reference is required\"");
        }

        paiement.setStatut(PaymentStatus.EN_ATTENTE);

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
