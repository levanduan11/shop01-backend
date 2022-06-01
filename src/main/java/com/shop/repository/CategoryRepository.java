package com.shop.repository;

import com.shop.model.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @EntityGraph(attributePaths = "children")
    Optional<Category> findByName(String name);

    @EntityGraph(value = "com.shop.category", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Category> findByAlias(String alias);

    Boolean existsByName(String name);

    Boolean existsByAlias(String alias);

    List<Category> findAllByParentIdIsNull();

}
