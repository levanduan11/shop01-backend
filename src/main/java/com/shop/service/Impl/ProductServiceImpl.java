package com.shop.service.Impl;

import com.shop.errors.ProductAliasAlreadyUsedException;
import com.shop.errors.ProductNameAlreadyUsedException;
import com.shop.model.product.Product;
import com.shop.model.product.ProductDetail;
import com.shop.model.product.ProductImage;
import com.shop.repository.BrandRepository;
import com.shop.repository.CategoryRepository;
import com.shop.repository.ProductRepository;
import com.shop.service.IProductService;
import com.shop.service.dto.product.ProductDTO;
import com.shop.service.dto.product.ProductDetailDTO;
import com.shop.service.dto.product.ProductForListDTO;
import com.shop.service.dto.product.ProductImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @Override
    public Product create(ProductDTO productDTO) {
        log.debug("create product {} ",productDTO);
        Optional<Product> existingName = productRepository.findByName(productDTO.getName());
        if (existingName.isPresent()) {
            throw new ProductNameAlreadyUsedException();
        }
        Optional<Product> existingAlias = productRepository.findByAlias(productDTO.getAlias());
        if (existingAlias.isPresent()) {
            throw new ProductAliasAlreadyUsedException();
        }
        Product product = new Product();
        product.setName(productDTO.getName());
        if (productDTO.getAlias() == null) {
            product.setAlias(productDTO.getName().replace(" ", "_"));
        }
        forCreateAndUpdate(productDTO, product);
        Set<ProductImage> productImages = new HashSet<>();
        Set<ProductImageDTO> imageDTOS = productDTO.getImageDTOS();
        if (imageDTOS != null) {
            imageDTOS.forEach(productImageDTO -> {
                ProductImage productImage = new ProductImage();
                productImage.setName(productImageDTO.getName());
                productImage.setProduct(product);
                productImages.add(productImage);
            });
        }
        product.setImages(productImages);
        Set<ProductDetail> productDetails = new HashSet<>();
        Set<ProductDetailDTO> detailDTOS = productDTO.getDetailDTOS();
        if (detailDTOS != null) {
            detailDTOS.forEach(productDetailDTO -> {
                ProductDetail productDetail = new ProductDetail();
                productDetail.setName(productDetailDTO.getName());
                productDetail.setValue(productDetailDTO.getValue());
                productDetail.setProduct(product);
                productDetails.add(productDetail);
            });
        }
        product.setDetails(productDetails);

        productRepository.save(product);

        return product;
    }

    private void forCreateAndUpdate(ProductDTO productDTO, Product product) {
        product.setShortDescription(productDTO.getShortDescription());
        product.setFullDescription(productDTO.getFullDescription());
        product.setEnabled(productDTO.isEnabled());
        product.setInStock(productDTO.isInStock());
        product.setCost(productDTO.getCost());
        product.setPrice(productDTO.getPrice());
        product.setDiscountPercent(productDTO.getDiscountPercent());

        product.setLength(productDTO.getLength());
        product.setWidth(productDTO.getWidth());
        product.setHeight(productDTO.getHeight());
        product.setWeight(productDTO.getWeight());

        product.setMainImage(productDTO.getMainImage());

        categoryRepository.findByName(productDTO.getCategory())
                .ifPresent((product::setCategory));
        brandRepository.findByName(productDTO.getBrand())
                .ifPresent(product::setBrand);
    }

    @Override
    public Optional<Product> update(ProductDTO productDTO) {
        return Optional.of(productRepository.findById(productDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(product -> {
                    forCreateAndUpdate(productDTO, product);
                    product.setUpdatedTime(Instant.now());
                    Set<ProductImage> productImages = product.getImages();
                    Set<ProductImageDTO> productImageDTOS = productDTO.getImageDTOS();
                    if (productImageDTOS != null) {
                        productImages.clear();
                        productImageDTOS.forEach(productImageDTO -> {
                            ProductImage productImage = new ProductImage();
                            productImage.setName(productImageDTO.getName());
                            productImage.setProduct(product);
                            productImages.add(productImage);
                        });

                    }
                    Set<ProductDetail> productDetails = product.getDetails();
                    Set<ProductDetailDTO> productDetailDTOS = productDTO.getDetailDTOS();
                    if (productDetailDTOS != null) {
                        productDetails.clear();
                        productDetailDTOS.forEach(productDetailDTO -> {
                            ProductDetail productDetail = new ProductDetail();
                            productDetail.setName(productDetailDTO.getName());
                            productDetail.setValue(productDetailDTO.getValue());
                            productDetail.setProduct(product);
                            productDetails.add(productDetail);
                        });
                    }
                    productRepository.save(product);

                    return product;
                });
    }

    @Override
    public Optional<Product> partialUpdate(ProductDTO productDTO) {
        return Optional.of(productRepository.findById(productDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(product -> {
                    if (productDTO.getName() != null) {
                        product.setName(productDTO.getName());
                    }
                    if (productDTO.getAlias() != null) {
                        product.setAlias(productDTO.getAlias());
                    }
                    if (productDTO.getShortDescription() != null) {
                        product.setShortDescription(productDTO.getShortDescription());
                    }
                    if (productDTO.getFullDescription() != null) {
                        product.setFullDescription(productDTO.getFullDescription());
                    }
                    if (productDTO.isEnabled()) {
                        product.setEnabled(productDTO.isEnabled());
                    }
                    if (productDTO.isInStock()) {
                        product.setInStock(productDTO.isInStock());
                    }
                    if (productDTO.getCost() != 0) {
                        product.setCost(productDTO.getCost());
                    }
                    if (productDTO.getPrice() != 0) {
                        product.setPrice(productDTO.getPrice());
                    }
                    if (productDTO.getDiscountPercent() != 0) {
                        product.setDiscountPercent(productDTO.getDiscountPercent());
                    }
                    if (productDTO.getLength() != 0) {
                        product.setLength(productDTO.getLength());
                    }
                    if (productDTO.getWidth() != 0) {
                        product.setWidth(productDTO.getWidth());
                    }
                    if (productDTO.getHeight() != 0) {
                        product.setHeight(productDTO.getHeight());
                    }
                    if (productDTO.getWeight() != 0) {
                        product.setWeight(productDTO.getWeight());
                    }
                    if (productDTO.getMainImage() != null) {
                        product.setMainImage(productDTO.getMainImage());
                    }
                    if (productDTO.getCategory() != null) {
                        categoryRepository.findByName(productDTO.getCategory())
                                .ifPresent((product::setCategory));
                    }
                    if (productDTO.getBrand() != null) {
                        brandRepository.findByName(productDTO.getBrand())
                                .ifPresent(product::setBrand);
                    }

                    Set<ProductImage> productImages = product.getImages();
                    Set<ProductImageDTO> productImageDTOS = productDTO.getImageDTOS();
                    if (productImageDTOS != null) {
                        productImages.clear();
                        productImageDTOS.forEach(productImageDTO -> {
                            ProductImage productImage = new ProductImage();
                            productImage.setName(productImageDTO.getName());
                            productImage.setProduct(product);
                            productImages.add(productImage);
                        });

                    }
                    Set<ProductDetail> productDetails = product.getDetails();
                    Set<ProductDetailDTO> productDetailDTOS = productDTO.getDetailDTOS();
                    if (productDetailDTOS != null) {
                        productDetails.clear();
                        productDetailDTOS.forEach(productDetailDTO -> {
                            ProductDetail productDetail = new ProductDetail();
                            productDetail.setName(productDetailDTO.getName());
                            productDetail.setValue(productDetailDTO.getValue());
                            productDetail.setProduct(product);
                            productDetails.add(productDetail);
                        });
                    }
                    productRepository.save(product);
                    return product;
                });
    }


    @Override
    public List<ProductForListDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductForListDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findOne(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::new);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
