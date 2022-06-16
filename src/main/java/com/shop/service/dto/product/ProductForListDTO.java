package com.shop.service.dto.product;

import com.shop.model.product.Product;

import java.time.Instant;

public class ProductForListDTO extends AbstractProductDTO {

    public ProductForListDTO() {
    }

    public ProductForListDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.createdTime = product.getCreatedTime();
        this.updatedTime = product.getUpdatedTime();
        this.category = product.getCategory().getName();
        this.brand = product.getBrand().getName();
        this.mainImage = product.getMainImage();
        this.enabled = product.isEnabled();

    }

}
