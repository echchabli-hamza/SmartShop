package com.SmartShop.SmartShop.entity;


import jakarta.persistence.Column;
import jakarta.persistence.*;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;




import java.time.LocalDate;

@Entity
@Table(name = "promo_codes")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String code;


    @Column(nullable = false)
    private Integer discountPercentage;

    private LocalDate dateCreation;


    private LocalDate dateExpiration;





    private boolean alreadyUsed = false;


    @OneToOne
    @JoinColumn(name = "used_in_order_id")
    private Commande usedInOrder;

}
