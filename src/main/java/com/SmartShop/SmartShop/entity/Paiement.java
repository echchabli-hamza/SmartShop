package com.SmartShop.SmartShop.entity;



import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.entity.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "paiements")
@Data
public class Paiement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    private Integer numeroPaiement;

    private Double montant;

    @Enumerated(EnumType.STRING)
    private PaymentStatus statut;

    private LocalDate datePaiement;
    private LocalDate dateEncaissement;

    private String reference;
    private String banque;

    @Enumerated(EnumType.STRING)
    private PaymentType typePaiement;
}
