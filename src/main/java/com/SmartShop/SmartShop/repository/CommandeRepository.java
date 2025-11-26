package com.SmartShop.SmartShop.repository;

import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByStatut(OrderStatus statut);
}
