package com.SmartShop.SmartShop.repository;

import com.SmartShop.SmartShop.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByCommandeId(Long commandeId);
}

