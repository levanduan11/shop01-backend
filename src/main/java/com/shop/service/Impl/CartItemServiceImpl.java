package com.shop.service.Impl;

import com.shop.errors.CartItemQuantityNotAvailableException;
import com.shop.errors.CustomerNotFoundException;
import com.shop.errors.ProductNotFoundException;
import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.model.product.Product;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import com.shop.service.CartItemService;
import com.shop.service.dto.cart.CartItemDTO;
import com.shop.service.dto.cart.UpdateQuantityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartItem create(CartItemDTO cartItemDTO) {

        User customer = userRepository.findById(cartItemDTO.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        Optional<CartItem> existingCartItem = cartItemRepository.findByCustomerIdAndProductId(
                cartItemDTO.getCustomerId(),
                cartItemDTO.getProductId()
        );
        CartItem cartItem = new CartItem();
        int availableQuantity = product.getUnitsInStock();
        int updateQuantity = cartItemDTO.getQuantity();
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            int oldQuantity = cartItem.getQuantity();
            updateQuantity += oldQuantity;
        } else {
            cartItem.setName(cartItemDTO.getName());
            cartItem.setImageUrl(cartItemDTO.getImageUrl());
            cartItem.setPrice(cartItemDTO.getPrice());
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);

        }
        if (updateQuantity > availableQuantity) {
            throw new CartItemQuantityNotAvailableException("This seller has a limit of " + availableQuantity + " per customer");
        }
        cartItem.setQuantity(updateQuantity);
        cartItemRepository.save(cartItem);
        log.info("save cart item {} ", cartItem);
        return cartItem;
    }

    @Override
    @Transactional
    public void updateQuantity(UpdateQuantityDTO quantityDTO) {
        Product product = productRepository.findById(quantityDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        int availableQuantity = product.getUnitsInStock();
        if (quantityDTO.getQuantity() > availableQuantity) {
            throw new CartItemQuantityNotAvailableException("This seller has a limit of " + availableQuantity + " per customer");
        }
        cartItemRepository.updateQuantity(quantityDTO.getQuantity(), quantityDTO.getCustomerId(), quantityDTO.getProductId());
    }


    @Override
    @Transactional
    public void deleteOne(Long customerId, Long productId) {
        cartItemRepository.deleteByCustomerAndProduct(customerId, productId);
    }

    @Override
    @Transactional
    public void deleteAll(Long customerId) {
        cartItemRepository.deleteByCustomer(customerId);
    }

    @Override
    public List<CartItemDTO> findAll(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId)
                .stream()
                .map(CartItemDTO::new)
                .collect(Collectors.toList());
    }
}
