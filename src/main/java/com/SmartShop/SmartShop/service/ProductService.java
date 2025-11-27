package com.SmartShop.SmartShop.service;



import com.SmartShop.SmartShop.dto.ProductDTO;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.OrderItemRepository;
import com.SmartShop.SmartShop.repository.ProductRepository;
import com.SmartShop.SmartShop.service.ProductService;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final SmartShopMapper mapper;

    public ProductService(ProductRepository productRepository, OrderItemRepository orderItemRepository, SmartShopMapper mapper) {
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
    }


    public ProductDTO addProduct(ProductDTO dto) {
        Product product = mapper.toProduct(dto);
        product.setDeleted(false);
        Product saved = productRepository.save(product);
        return mapper.toProductDTO(saved);
    }


    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        product.setNom(dto.getNom());
        product.setPrixUnitaire(dto.getPrixUnitaire());
        product.setStockDisponible(dto.getStockDisponible());


        Product saved = productRepository.save(product);
        return mapper.toProductDTO(saved);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        boolean usedInOrders = orderItemRepository.existsByProductId(id);

        if (usedInOrders) {
            // soft delete
            product.setDeleted(true);
            productRepository.save(product);
        } else {

            productRepository.delete(product);
        }
    }


    public Page<ProductDTO> getProducts(Pageable p) {



        Page<Product> pageResult = productRepository.findAll(p);

        return pageResult.map(mapper::toProductDTO);
    }
}
