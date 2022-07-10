package com.shop.service.dto.cart;

import com.shop.model.CartItem;

public class CartItemDTO {

    private Long id;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;
    private Long customerId;
    private Long productId;


    public CartItemDTO() {
    }

    public CartItemDTO(CartItem cartItem) {
        this.id = cartItem.getId();
        this.name=cartItem.getName();
        this.price=cartItem.getPrice();
        this.imageUrl=cartItem.getImageUrl();
        this.quantity=cartItem.getQuantity();
        this.customerId=cartItem.getCustomer().getId();
        this.productId=cartItem.getProduct().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
