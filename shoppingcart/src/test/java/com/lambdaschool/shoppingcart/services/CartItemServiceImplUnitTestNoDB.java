package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.ShoppingCartApplicationTests;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.CartItemId;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.repository.CartItemRepository;
import com.lambdaschool.shoppingcart.repository.ProductRepository;
import com.lambdaschool.shoppingcart.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShoppingCartApplicationTests.class)
public class CartItemServiceImplUnitTestNoDB {
    @Autowired
    CartItemService cartItemService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    CartItemRepository cartItemRepository;

    List<User> users = new ArrayList<>();
    List<Product> products = new ArrayList<>();
    List<CartItem> items = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        User user1 = new User(
                "username",
                "password",
                "primary@email.com",
                "");
        user1.setUserid(1);
        users.add(user1);

        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(100.0);
        product1.setDescription("Product 1 Description");
        product1.setProductid(1);
        products.add(product1);

        CartItem cartItem1 = new CartItem(user1,
                product1,
                2,
                "");
        items.add(cartItem1);

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addToCart() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(users.get(0)));
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(products.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class)))
                .thenReturn(Optional.of(items.get(0)));

        cartItemService.addToCart(1, 1, "");
        assertEquals(1, items.size());

    }

    @Test
    public void removeFromCart() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(users.get(0)));
        Mockito.when(productRepository.findById(1L))
                .thenReturn(Optional.of(products.get(0)));
        Mockito.when(cartItemRepository.findById(any(CartItemId.class)))
                .thenReturn(Optional.of(items.get(0)));
        Mockito.doNothing()
                .when(cartItemRepository)
                .deleteById(any(CartItemId.class));

        cartItemService.removeFromCart(1, 1, "");
        assertEquals(1, items.size());
    }
}