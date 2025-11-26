package com.SmartShop.SmartShop.entity;





import com.SmartShop.SmartShop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "commandes")
public class Commande {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDateTime date;

    private Double sousTotal;
    private Double remise;
    private Double tva;
    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus statut;


    private Double montantRestant;


    private String promoCode;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<Paiement> paiements;
}
