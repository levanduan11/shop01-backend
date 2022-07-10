package com.shop.repository;

import com.shop.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @EntityGraph(value = "cartItem")
    List<CartItem> findByCustomerId(Long customerId);

    @EntityGraph(value = "cartItem")
    Optional<CartItem> findByCustomerIdAndProductId(Long customerId, Long productId);

    @Modifying
    @Query("UPDATE CartItem c SET c.quantity=?1 WHERE c.customer.id=?2 AND c.product.id=?3")
    void updateQuantity(int quantity,Long customerId,Long productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id=?1 AND c.product.id=?2")
    void deleteByCustomerAndProduct(Long customerId,Long productId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.customer.id=?1")
    void deleteByCustomer(Long customerId);

}
