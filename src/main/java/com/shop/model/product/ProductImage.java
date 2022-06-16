package com.shop.model.product;

import com.shop.model.IDBased;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_images")
public class ProductImage extends IDBased {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductImage)) return false;
        if (!super.equals(o)) return false;
        ProductImage that = (ProductImage) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
