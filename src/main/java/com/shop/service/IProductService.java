package com.shop.service;

import com.shop.model.Brand;
import com.shop.model.product.Product;
import com.shop.repository.ProductRepository;
import com.shop.service.dto.product.ProductDTO;
import com.shop.service.dto.product.ProductForListDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IProductService {

    Product create(ProductDTO productDTO);

    Optional<Product> update(ProductDTO productDTO);

    Optional<Product> partialUpdate(ProductDTO productDTO);

    List<ProductForListDTO> findAll();

    Optional<ProductDTO> findOne(Long id);

    void delete(Long id);
}
