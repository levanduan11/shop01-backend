package com.shop.service;

import com.shop.model.CartItem;
import com.shop.service.dto.cart.CartItemDTO;
import com.shop.service.dto.cart.UpdateQuantityDTO;

import java.util.List;

public interface CartItemService {
    CartItem create(CartItemDTO cartItemDTO);
    void updateQuantity(UpdateQuantityDTO quantityDTO);
    void deleteOne(Long customerId,Long productId);
    void deleteAll(Long customerId);
    List<CartItemDTO>findAll(Long customerId);


}
