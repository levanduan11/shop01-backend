package com.shop.repository;

import com.shop.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product>findByName(String name);
    Optional<Product>findByAlias(String alias);
}
