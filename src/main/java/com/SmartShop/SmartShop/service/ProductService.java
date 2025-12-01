package com.SmartShop.SmartShop.service;



import com.SmartShop.SmartShop.dto.ProductDTO;
import com.SmartShop.SmartShop.entity.Product;
import com.SmartShop.SmartShop.exception.ResourceNotFoundException;
import com.SmartShop.SmartShop.helper.ProductSpecs;
import com.SmartShop.SmartShop.mapper.SmartShopMapper;
import com.SmartShop.SmartShop.repository.OrderItemRepository;
import com.SmartShop.SmartShop.repository.ProductRepository;


import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
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
                .orElseThrow(() ->  new ResourceNotFoundException("Payment not found with id : " +  id));

        product.setNom(dto.getNom());
        product.setPrixUnitaire(dto.getPrixUnitaire());
        product.setStockDisponible(dto.getStockDisponible());


        Product saved = productRepository.save(product);
        return mapper.toProductDTO(saved);
    }


    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id : " +  id));

        boolean usedInOrders = orderItemRepository.existsByProductId(id);

        if (usedInOrders) {

            product.setDeleted(true);
            productRepository.save(product);
        } else {

            productRepository.delete(product);
        }
    }


    public Page<ProductDTO> getProducts(String nom, Double minPrice , Integer q, Pageable p) {
        Specification<Product> spec = Specification
                .where(ProductSpecs.hasMinQuantity(q))
                .and(ProductSpecs.hasName(nom))
                .and(ProductSpecs.minPrice(minPrice));

        return productRepository.findAll(spec, p)
                .map(mapper::toProductDTO);
    }

}
