package com.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shop.model.product.Product;

import javax.persistence.*;

@Entity
@Table(name = "cart_items")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "cartItem",attributeNodes = {
                @NamedAttributeNode(value = "customer"),
                @NamedAttributeNode(value = "product"),
        })
})
public class CartItem extends IDBased{

    private String name;
    private double price;
    private String imageUrl;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private User customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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


    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                '}';
    }
}
