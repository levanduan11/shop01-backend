package com.shop.errors;

import com.shop.model.product.Product;

public class ProductNameAlreadyUsedException extends RuntimeException{

    public ProductNameAlreadyUsedException() {
        super("product name already used !");
    }


}
