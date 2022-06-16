package com.shop.product;

import com.shop.model.product.Product;
import com.shop.service.Impl.ProductServiceImpl;
import com.shop.service.dto.product.ProductDTO;
import com.shop.service.dto.product.ProductDetailDTO;
import com.shop.service.dto.product.ProductImageDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductServiceImpl productService;


    @Test
    public void create(){
        ProductDTO productDTO=new ProductDTO();
        productDTO.setName("abcde");
        productDTO.setShortDescription("good");
        productDTO.setFullDescription("good");
        productDTO.setCategory("Electronics");
        productDTO.setBrand("Canon");

        Set<ProductDetailDTO> detailDTOS=new HashSet<>();
        ProductDetailDTO productDetailDTO=new ProductDetailDTO();
        productDetailDTO.setName("aa");
        productDetailDTO.setValue("aa");
        detailDTOS.add(productDetailDTO);

        ProductDetailDTO productDetailDTO1=new ProductDetailDTO();
        productDetailDTO1.setName("bb");
        productDetailDTO1.setValue("bb");
        detailDTOS.add(productDetailDTO1);

        productDTO.setDetailDTOS(detailDTOS);



        Set<ProductImageDTO>imageDTOS=new HashSet<>();
        ProductImageDTO productImageDTO=new ProductImageDTO();
        productImageDTO.setName("image1");
        imageDTOS.add(productImageDTO);

        ProductImageDTO productImageDTO1=new ProductImageDTO();
        productImageDTO1.setName("image2");
        imageDTOS.add(productImageDTO1);


        productDTO.setImageDTOS(imageDTOS);

      Product creted= productService.create(productDTO);
      assertThat(creted.getId()).isGreaterThan(0);

    }

    @Test
    public void update(){
        ProductDTO productDTO=new ProductDTO();
        productDTO.setId(6l);
        productDTO.setName("abcde");
        productDTO.setShortDescription("good");
        productDTO.setFullDescription("good");
        productDTO.setCategory("Electronics");
        productDTO.setBrand("Canon");

        Set<ProductDetailDTO> detailDTOS=new HashSet<>();
        ProductDetailDTO productDetailDTO=new ProductDetailDTO();
        productDetailDTO.setName("aa");
        productDetailDTO.setValue("aa");
        detailDTOS.add(productDetailDTO);

        ProductDetailDTO productDetailDTO1=new ProductDetailDTO();
        productDetailDTO1.setName("bb");
        productDetailDTO1.setValue("bb");
        detailDTOS.add(productDetailDTO1);

        productDTO.setDetailDTOS(detailDTOS);



        Set<ProductImageDTO>imageDTOS=new HashSet<>();
        ProductImageDTO productImageDTO=new ProductImageDTO();
        productImageDTO.setName("image1");
        imageDTOS.add(productImageDTO);

        ProductImageDTO productImageDTO1=new ProductImageDTO();
        productImageDTO1.setName("image2");
        imageDTOS.add(productImageDTO1);


        productDTO.setImageDTOS(imageDTOS);

        Optional<Product> update= productService.update(productDTO);
        assertThat(update.get().getId()).isEqualTo(6l);

    }

    @Test
    public void all(){
        List<ProductDTO>productDTOS=productService.findAll();
       productDTOS.forEach(x->{
           System.out.println(x);
       });
    }


}
