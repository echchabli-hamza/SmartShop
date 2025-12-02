package com.SmartShop.SmartShop.unit;

import com.SmartShop.SmartShop.dto.PaiementDTO;
import com.SmartShop.SmartShop.entity.Commande;
import com.SmartShop.SmartShop.entity.Paiement;
import com.SmartShop.SmartShop.entity.enums.OrderStatus;
import com.SmartShop.SmartShop.entity.enums.PaymentStatus;
import com.SmartShop.SmartShop.entity.enums.PaymentType;
import com.SmartShop.SmartShop.exception.BusinessException;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.CommandeRepository;
import com.SmartShop.SmartShop.repository.PaiementRepository;
import com.SmartShop.SmartShop.service.PaymentService;
import com.SmartShop.SmartShop.service.strategy.PaymentStrategy;
import com.SmartShop.SmartShop.service.strategy.impl.PaymentStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaiementRepository paiementRepository;

    @Mock
    private PaymentStrategyFactory strategyFactory;

    @Mock
    private SmartShopMapper smartShopMapper;

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Commande commande;
    private Paiement paiement;
    private PaiementDTO paiementDTO;

    @BeforeEach
    void setUp() {
        commande = new Commande();
        commande.setId(1L);
        commande.setTotal(1000.0);
        commande.setMontantRestant(1000.0);
        commande.setPaiements(new ArrayList<>());

        paiement = new Paiement();
        paiement.setId(1L);
        paiement.setMontant(500.0);
        paiement.setTypePaiement(PaymentType.ESPECES);
        paiement.setStatut(PaymentStatus.EN_ATTENTE);
        paiement.setCommande(commande);

        paiementDTO = new PaiementDTO();
        paiementDTO.setId(1L);
        paiementDTO.setMontant(500.0);
        paiementDTO.setTypePaiement("ESPECES");
        paiementDTO.setCommandeId(1);
    }

    @Test
    void addPayment_success() {
        // Given
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(smartShopMapper.toPaiement(paiementDTO)).thenReturn(paiement);
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(i -> i.getArguments()[0]);
        when(smartShopMapper.toPaiementDTO(any(Paiement.class))).thenReturn(paiementDTO);
        // Mock strategy factory
        PaymentStrategy mockStrategy = mock(PaymentStrategy.class);
        doNothing().when(mockStrategy).addPayment(any(Paiement.class));
        when(strategyFactory.getStrategy(any(PaymentType.class))).thenReturn(mockStrategy);

        // When
        PaiementDTO result = paymentService.addPayment(paiementDTO);

        // Then
        assertNotNull(result);
        verify(commandeRepository, times(1)).findById(1L);
        verify(paiementRepository, times(1)).save(any(Paiement.class));
    }

    @Test
    void addPayment_amountExceedsRemaining() {
        // Given
        paiementDTO.setMontant(2000.0); // More than remaining
        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> paymentService.addPayment(paiementDTO)
        );

        assertTrue(exception.getMessage().contains("dépasse le montant restant"));
        verify(paiementRepository, never()).save(any());
    }

    @Test
    void addPayment_commandeNotFound() {
        // Given
        paiementDTO.setCommandeId(999);
        when(commandeRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> paymentService.addPayment(paiementDTO)
        );

        assertTrue(exception.getMessage().contains("Commande introuvable"));
    }

    @Test
    void encaisserPayment_success() {
        // Given
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(commandeRepository.save(any(Commande.class))).thenAnswer(i -> i.getArgument(0));
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(i -> i.getArguments()[0]);
        when(smartShopMapper.toPaiementDTO(any(Paiement.class))).thenReturn(paiementDTO);
        // Mock strategy
        PaymentStrategy mockStrategy = mock(PaymentStrategy.class);
        doNothing().when(mockStrategy).encaisserPayment(any(Paiement.class));
        when(strategyFactory.getStrategy(any(PaymentType.class))).thenReturn(mockStrategy);

        // When
        PaiementDTO result = paymentService.encaisserPayment(1L);

        // Then
        assertNotNull(result);
        verify(paiementRepository, times(1)).findById(1L);
        verify(commandeRepository, times(1)).save(any(Commande.class));
    }

    @Test
    void rejectPayment_success() {
        // Given
        paiement.setStatut(PaymentStatus.EN_ATTENTE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));
        when(paiementRepository.save(any(Paiement.class))).thenAnswer(i -> i.getArguments()[0]);
        when(smartShopMapper.toPaiementDTO(any(Paiement.class))).thenReturn(paiementDTO);
        // Mock strategy
        PaymentStrategy mockStrategy = mock(PaymentStrategy.class);
        doNothing().when(mockStrategy).rejectPayment(any(Paiement.class));
        when(strategyFactory.getStrategy(any(PaymentType.class))).thenReturn(mockStrategy);

        // When
        PaiementDTO result = paymentService.rejectPayment(1L);

        // Then
        assertNotNull(result);
        verify(paiementRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPayment_alreadyEncaisse() {
        // Given
        paiement.setStatut(PaymentStatus.ENCAISSE);
        when(paiementRepository.findById(1L)).thenReturn(Optional.of(paiement));

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> paymentService.rejectPayment(1L)
        );

        assertTrue(exception.getMessage().contains("Payment already confirmed"));
    }
}

