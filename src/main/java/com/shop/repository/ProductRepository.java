package com.shop.repository;

import com.shop.model.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @EntityGraph(value = "product.list")
    Optional<Product> findByAlias(String alias);

    @Query("SELECT p FROM Product p WHERE p.enabled=true " +
            " AND (p.category.id = ?1 OR p.category.allParentId LIKE %?2%) " +
            " ORDER BY p.name ASC")
    @EntityGraph(value = "product.list")
    Page<Product> listByCategory(Long categoryId, String categoryIdMatch, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE enabled = true AND "
            + "MATCH(name, short_description, full_description) AGAINST (?1)",
            nativeQuery = true)
    Page<Product> search(String keyword, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from products order by created_time desc limit 12;")
    List<Product> findTop12OrderByCreatedTimeDesc();

    Page<Product>findAllByBrandId(Long brandId,Pageable pageable);

}
