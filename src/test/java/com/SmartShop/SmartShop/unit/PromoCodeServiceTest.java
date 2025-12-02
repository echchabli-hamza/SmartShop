package com.SmartShop.SmartShop.unit;

import com.SmartShop.SmartShop.entity.PromoCode;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.repository.PromoCodeRepository;
import com.SmartShop.SmartShop.service.PromoCodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @InjectMocks
    private PromoCodeService promoCodeService;

    private PromoCode promoCode;

    @BeforeEach
    void setUp() {
        promoCode = new PromoCode();
        promoCode.setId(1L);
        promoCode.setCode("PROMO-ABCD");
        promoCode.setDiscountPercentage(10);
        promoCode.setDateCreation(LocalDate.now());
        promoCode.setDateExpiration(LocalDate.now().plusDays(30));
        promoCode.setAlreadyUsed(false);
    }

    @Test
    void getAllPromoCodes_success() {
        // Given
        List<PromoCode> promoCodes = Arrays.asList(promoCode);
        when(promoCodeRepository.findAll()).thenReturn(promoCodes);

        // When
        List<PromoCode> result = promoCodeService.getAllPromoCodes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(promoCodeRepository, times(1)).findAll();
    }

    @Test
    void getPromoCodeById_success() {
        // Given
        Long promoId = 1L;
        when(promoCodeRepository.findById(promoId)).thenReturn(Optional.of(promoCode));

        // When
        PromoCode result = promoCodeService.getPromoCodeById(promoId);

        // Then
        assertNotNull(result);
        assertEquals("PROMO-ABCD", result.getCode());
        verify(promoCodeRepository, times(1)).findById(promoId);
    }

    @Test
    void getPromoCodeById_notFound() {
        // Given
        Long promoId = 999L;
        when(promoCodeRepository.findById(promoId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> promoCodeService.getPromoCodeById(promoId)
        );

        assertTrue(exception.getMessage().contains("PromoCode not found"));
    }

    @Test
    void createPromoCode_success() {
        // Given
        PromoCode newPromo = new PromoCode();
        newPromo.setCode("PROMO-NEW1");
        newPromo.setDiscountPercentage(15);
        newPromo.setDateExpiration(LocalDate.now().plusDays(60));

        when(promoCodeRepository.save(any(PromoCode.class))).thenAnswer(i -> {
            PromoCode p = i.getArgument(0);
            p.setId(2L);
            p.setDateCreation(LocalDate.now());
            p.setAlreadyUsed(false);
            return p;
        });

        // When
        PromoCode result = promoCodeService.createPromoCode(newPromo);

        // Then
        assertNotNull(result);
        assertNotNull(result.getDateCreation());
        assertFalse(result.isAlreadyUsed());
        verify(promoCodeRepository, times(1)).save(any(PromoCode.class));
    }

    @Test
    void updatePromoCode_success() {
        // Given
        Long promoId = 1L;
        PromoCode updatedPromo = new PromoCode();
        updatedPromo.setDiscountPercentage(20);
        updatedPromo.setDateExpiration(LocalDate.now().plusDays(45));

        when(promoCodeRepository.findById(promoId)).thenReturn(Optional.of(promoCode));
        when(promoCodeRepository.save(any(PromoCode.class))).thenAnswer(i -> i.getArgument(0));

        // When
        PromoCode result = promoCodeService.updatePromoCode(promoId, updatedPromo);

        // Then
        assertNotNull(result);
        assertEquals(20, result.getDiscountPercentage());
        verify(promoCodeRepository, times(1)).findById(promoId);
        verify(promoCodeRepository, times(1)).save(promoCode);
    }

    @Test
    void deletePromoCode_success() {
        // Given
        Long promoId = 1L;
        doNothing().when(promoCodeRepository).deleteById(promoId);

        // When
        promoCodeService.deletePromoCode(promoId);

        // Then
        verify(promoCodeRepository, times(1)).deleteById(promoId);
    }
}

