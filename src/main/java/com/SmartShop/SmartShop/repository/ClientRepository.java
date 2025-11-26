package com.SmartShop.SmartShop.repository;


import com.SmartShop.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    // Recherche par email
    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);


}


