package com.SmartShop.SmartShop.entity;


import com.SmartShop.SmartShop.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToOne(mappedBy = "user")
    private Client client;
}
