package com.shop.repository;

import com.shop.model.Brand;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @EntityGraph(attributePaths = "categories")
    Optional<Brand> findByName(String name);

    Boolean existsByName(String name);

}
