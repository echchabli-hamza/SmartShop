package com.SmartShop.SmartShop.service.helper;

import com.SmartShop.SmartShop.entity.OrderItem;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class CheckStock {
      OrderItemRepository orderItemRepository;
    public void validateStock(List<OrderItem> l ) {

           for (OrderItem item : l) {
            Product p = item.getProduct();

            if (p.getStockDisponible() < item.getQuantite()) {
                throw new BusinessException("Stock insuffisant pour le produit : " + p.getNom() + " (ID : " + p.getId() + ")"
                );
            }
        }
    }

}
