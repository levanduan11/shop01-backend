package com.shop.repository;

import com.shop.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User>findByUsername(String username);
    Optional<User>findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User>findOneByActivationKey(String activationKey);
    List<User>findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User>findOneByResetKey(String resetKey);
    Optional<User>findOneByEmailIgnoreCase(String email);
    Optional<User>findOneByUsername(String username);
    @EntityGraph(attributePaths = "authorities")
    Optional<User>findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    Optional<User>findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User>findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

}
