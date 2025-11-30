package com.SmartShop.SmartShop.service.helper;


import com.SmartShop.SmartShop.entity.Client;
import com.SmartShop.SmartShop.entity.Commande;

import com.SmartShop.SmartShop.entity.enums.CustomerTier;

import java.time.LocalDate;

public class UpdateC {


    public static void updateClientData(Client client, Commande commande) {


        client.setLastOrderDate(LocalDate.now());
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent() + commande.getTotal());

        client.setNiveau(determineNiveau(client));
    }



    private static CustomerTier determineNiveau(Client client) {

        int orders = client.getTotalOrders();
        double spent = client.getTotalSpent();

        if (orders >= 20 || spent >= 15000) {
            return CustomerTier.PLATINUM;
        }
        if (orders >= 10 || spent >= 5000) {
            return CustomerTier.GOLD;
        }
        if (orders >= 3 || spent >= 1000) {
            return CustomerTier.SILVER;
        }

        return CustomerTier.BASIC;
    }
}
