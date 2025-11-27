package com.SmartShop.SmartShop.repository;

import com.SmartShop.SmartShop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCommandeId(Long commandeId);
    boolean existsByProductId(Long productId);
}
