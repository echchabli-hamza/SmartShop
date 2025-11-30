package com.SmartShop.SmartShop.helper;

import com.SmartShop.SmartShop.entity.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecs {

    public static Specification<Product> hasName(String nom) {
        return (root, query, cb) ->
                (nom == null || nom.isEmpty())
                        ? null
                        : cb.like(cb.lower(root.get("nom")), "%" + nom.toLowerCase() + "%");
    }

    public static Specification<Product> minPrice(Double min) {
        return (root, query, cb) ->
                min == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("prixUnitaire"), min);
    }

    public static Specification<Product> hasMinQuantity(Integer quantity) {
        return (root, query, cb) ->
                (quantity == null)
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("stockDisponible"), quantity);
    }

}
