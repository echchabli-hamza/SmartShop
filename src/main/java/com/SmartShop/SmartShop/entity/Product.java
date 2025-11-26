package com.SmartShop.SmartShop.entity;




import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "products")
@Data
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private Double prixUnitaire;

    private Integer stockDisponible;

    private boolean deleted = false;
}
