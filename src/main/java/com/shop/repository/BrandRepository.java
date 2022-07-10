package com.shop.repository;

import com.shop.model.Brand;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @EntityGraph(attributePaths = "categories")
    Optional<Brand> findByName(String name);

    Boolean existsByName(String name);

    @Query(nativeQuery = true, value = "select distinct b.id,b.name,b.logo\n" +
            " from brands b\n" +
            " inner join products p on b.id=p.brand_id\n" +
            " limit 12;")
    List<Brand> findFirst12();

}
