package com.SmartShop.SmartShop.entity;




import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "order_items")
@Data
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantite;

    private Double prixUnitaire;

    private Double totalLigne;




}
