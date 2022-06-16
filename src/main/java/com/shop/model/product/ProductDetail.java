package com.shop.model.product;

import com.shop.model.IDBased;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_details")
public class ProductDetail extends IDBased {

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = false,length = 255)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(o instanceof ProductDetail)) return false;
        if (!super.equals(o)) return false;
        ProductDetail that = (ProductDetail) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
