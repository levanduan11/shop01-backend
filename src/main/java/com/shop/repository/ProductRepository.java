package com.shop.repository;

import com.shop.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product>findByName(String name);
    Optional<Product>findByAlias(String alias);

    @Query("SELECT p FROM Product p WHERE p.enabled=true " +
            " AND (p.category.id = ?1 OR p.category.allParentId LIKE %?2%) " +
            " ORDER BY p.name ASC")
    @EntityGraph(value = "product.list")
    Page<Product>listByCategory(Long categoryId, String categoryIdMatch, Pageable pageable);
}
