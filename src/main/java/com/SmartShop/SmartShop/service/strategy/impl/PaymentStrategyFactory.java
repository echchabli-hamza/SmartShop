package com.SmartShop.SmartShop.service.strategy.impl;



import com.SmartShop.SmartShop.entity.enums.PaymentType;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    public PaymentStrategy getStrategy(PaymentType type) {
        return switch (type) {
            case ESPECES -> new CashPaymentStrategy();
            case CHEQUE -> new CheckPaymentStrategy();
            case VIREMENT -> new BankTransferPaymentStrategy();
        };
    }
}
