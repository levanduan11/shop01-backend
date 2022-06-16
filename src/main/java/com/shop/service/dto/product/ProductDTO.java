package com.shop.service.dto.product;


import com.shop.model.product.Product;


import javax.persistence.Column;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDTO extends AbstractProductDTO {


    @Column(unique = true, nullable = false, length = 255)
    protected String alias;
    @Column(length = 512)
    private String shortDescription;

    @Column(length = 5999)
    private String fullDescription;

    private boolean inStock;
    private double cost;
    private double price;

    private double discountPercent;

    private float length;
    private float width;
    private float height;
    private float weight;

    private Set<ProductDetailDTO> detailDTOS;
    private Set<ProductImageDTO> imageDTOS;

    public ProductDTO() {

    }


    public ProductDTO (Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.alias = product.getAlias();
        this.shortDescription = product.getShortDescription();
        this.fullDescription = product.getFullDescription();
        this.createdTime = product.getCreatedTime();
        this.updatedTime = product.getUpdatedTime();
        this.enabled = product.isEnabled();
        this.inStock = product.isInStock();
        this.cost = product.getCost();
        this.price = product.getPrice();
        this.discountPercent = product.getDiscountPercent();
        this.length = product.getLength();
        this.width = product.getWidth();
        this.height = product.getHeight();
        this.weight = product.getWeight();
        this.category = product.getCategory().getName();
        this.brand = product.getBrand().getName();
        this.mainImage = product.getMainImage();
        this.imageDTOS = product.getImages().stream().map(productImage -> {
            ProductImageDTO productImageDTO = new ProductImageDTO();
            productImageDTO.setName(productImage.getName());
            return productImageDTO;
        }).collect(Collectors.toSet());
        this.detailDTOS = product.getDetails().stream().map(productDetail -> {
            ProductDetailDTO productDetailDTO = new ProductDetailDTO();
            productDetailDTO.setName(productDetail.getName());
            productDetailDTO.setValue(productDetail.getValue());
            return productDetailDTO;
        }).collect(Collectors.toSet());


    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }


    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    public Set<ProductDetailDTO> getDetailDTOS() {
        return detailDTOS;
    }

    public void setDetailDTOS(Set<ProductDetailDTO> detailDTOS) {
        this.detailDTOS = detailDTOS;
    }

    public Set<ProductImageDTO> getImageDTOS() {
        return imageDTOS;
    }

    public void setImageDTOS(Set<ProductImageDTO> imageDTOS) {
        this.imageDTOS = imageDTOS;
    }


}
