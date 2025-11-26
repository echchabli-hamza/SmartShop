package com.SmartShop.SmartShop.entity;



import com.SmartShop.SmartShop.entity.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "clients")
public class Client {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier niveau;

    private Integer totalOrders = 0;
    private Double totalSpent = 0.0;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "client")
    private List<Commande> commandes;
}
