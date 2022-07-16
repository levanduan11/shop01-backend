package com.shop.cart_item;

import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.model.product.Product;
import com.shop.repository.CartItemRepository;
import com.shop.repository.ProductRepository;
import com.shop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepoTest {

    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void create(){
        Long customerId=30l;
        Long productId=1l;
        User user=userRepository.findById(customerId).get();
        Product product=productRepository.findById(productId).get();

        CartItem cartItem=new CartItem();

        cartItem.setName(product.getName());
        cartItem.setPrice(product.getPrice());
        cartItem.setImageUrl(product.getMainImage());
        cartItem.setQuantity(9);
        cartItem.setCustomer(user);
        cartItem.setProduct(product);
        CartItem created=cartItemRepository.save(cartItem);
        assertThat(created.getId()).isGreaterThan(0);

    }
    @Test
    public void findByCustomer(){
        Long customerId=30l;

        List<CartItem>cartItem=cartItemRepository.findByCustomerId(customerId);
    cartItem.forEach(System.out::println);

    }
    @Test
    public void findByCustomerAndProduct(){
        Long customerId=30l;
        Long productId=1l;

        Optional<CartItem>cartItem=cartItemRepository.findByCustomerIdAndProductId(customerId,productId);
        System.out.println(cartItem.get());

    }
}
