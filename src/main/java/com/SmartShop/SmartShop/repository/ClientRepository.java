package com.SmartShop.SmartShop.repository;


import com.SmartShop.SmartShop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUserId(Long userId);

    boolean existsByEmail(String email);


}


