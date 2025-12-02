package com.SmartShop.SmartShop.unit;

import com.SmartShop.SmartShop.dto.ProductDTO;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.OrderItemRepository;
import com.SmartShop.SmartShop.repository.ProductRepository;
import com.SmartShop.SmartShop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private SmartShopMapper mapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setNom("Test Product");
        product.setPrixUnitaire(100.0);
        product.setStockDisponible(50);
        product.setDeleted(false);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setNom("Test Product");
        productDTO.setPrixUnitaire(100.0);
        productDTO.setStockDisponible(50);
    }

    @Test
    void addProduct_success() {
        // Given
        when(mapper.toProduct(productDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> {
            Product p = i.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(mapper.toProductDTO(any(Product.class))).thenReturn(productDTO);

        // When
        ProductDTO result = productService.addProduct(productDTO);

        // Then
        assertNotNull(result);
        assertFalse(product.isDeleted());
        verify(mapper, times(1)).toProduct(productDTO);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_success() {
        // Given
        Long productId = 1L;
        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setNom("Updated Product");
        updatedDTO.setPrixUnitaire(150.0);
        updatedDTO.setStockDisponible(75);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        when(mapper.toProductDTO(any(Product.class))).thenReturn(updatedDTO);

        // When
        ProductDTO result = productService.updateProduct(productId, updatedDTO);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_notFound() {
        // Given
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> productService.updateProduct(productId, productDTO)
        );

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void deleteProduct_notUsedInOrders() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId(productId)).thenReturn(false);
        doNothing().when(productRepository).delete(product);

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).delete(product);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_usedInOrders() {
        // Given
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId(productId)).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // When
        productService.deleteProduct(productId);

        // Then
        assertTrue(product.isDeleted());
        verify(productRepository, times(1)).save(product);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void getProducts_success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = List.of(product);
        Page<Product> productPage = new PageImpl<>(products, pageable, 1);

        when(productRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(productPage);
        when(mapper.toProductDTO(any(Product.class))).thenReturn(productDTO);

        // When
        Page<ProductDTO> result = productService.getProducts(null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getProducts_withFilters() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String nom = "Test";
        Double minPrice = 50.0;
        Integer q = 10;

        when(productRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(new ArrayList<>(), pageable, 0));

        // When
        Page<ProductDTO> result = productService.getProducts(nom, minPrice, q, pageable);

        // Then
        assertNotNull(result);
        verify(productRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }
}

