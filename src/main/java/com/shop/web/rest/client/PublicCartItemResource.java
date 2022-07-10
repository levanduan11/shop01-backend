package com.shop.web.rest.client;

import com.shop.model.CartItem;
import com.shop.repository.CartItemRepository;
import com.shop.service.CartItemService;
import com.shop.service.dto.cart.CartItemDTO;
import com.shop.service.dto.cart.UpdateQuantityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/client")
public class PublicCartItemResource {
    private final Logger log = LoggerFactory.getLogger(PublicCartItemResource.class);
    private final CartItemService cartItemService;
    private final CartItemRepository cartItemRepository;

    public PublicCartItemResource(CartItemService cartItemService, CartItemRepository cartItemRepository) {
        this.cartItemService = cartItemService;
        this.cartItemRepository = cartItemRepository;
    }

    @PostMapping("/cart_item")
    public ResponseEntity<CartItem> create(@RequestBody CartItemDTO cartItemDTO) {
        log.debug("create from request {} ", cartItemDTO);
        CartItem cartItem = cartItemService.create(cartItemDTO);
        return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/cart_item")
    public ResponseEntity<Void> updateQuantity(@RequestBody UpdateQuantityDTO quantityDTO) {
        cartItemService.updateQuantity(quantityDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cart_item/{customerId}")
    public ResponseEntity<List<CartItemDTO>> findAll(@PathVariable final Long customerId) {
        List<CartItemDTO> cartItemDTOS = cartItemService.findAll(customerId);
        return ResponseEntity.ok(cartItemDTOS);
    }

    @DeleteMapping("/cart_item/{customerId}/{productId}")
    public ResponseEntity<Void> deleteOne(@PathVariable final Long customerId, @PathVariable final Long productId) {
        log.debug("delete cart item product id: {}", productId);
        cartItemService.deleteOne(customerId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart_item/{customerId}")
    public ResponseEntity<Void> deleteAll(@PathVariable final Long customerId) {
        log.debug("delete all cart item customer id: {}", customerId);
        cartItemService.deleteAll(customerId);
        return ResponseEntity.noContent().build();
    }

}
