package com.SmartShop.SmartShop.service.strategy;

import com.SmartShop.SmartShop.entity.Paiement;

public interface PaymentStrategy {
    void addPayment(Paiement paiement);
    void encaisserPayment(Paiement paiement);
    void rejectPayment(Paiement paiement);
}
