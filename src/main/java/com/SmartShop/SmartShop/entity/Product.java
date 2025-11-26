package com.SmartShop.SmartShop.entity;




import jakarta.persistence.*;


@Entity
@Table(name = "products")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Double prixUnitaire;

    private Integer stockDisponible;

    private boolean deleted = false;
}
