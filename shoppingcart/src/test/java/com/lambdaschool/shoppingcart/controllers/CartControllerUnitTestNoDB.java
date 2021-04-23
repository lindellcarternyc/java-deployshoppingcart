package com.lambdaschool.shoppingcart.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.Product;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.SecurityContextService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WithMockUser(username = "admin",
    roles = {"USER", "ADMIN"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ShoppingCartApplicationTest.class,
    properties = "command.line.runner.enabled=false")
public class CartControllerUnitTestNoDB {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    SecurityContextService securityContextService;

    @MockBean
    CartItemService cartItemService;

    private User testUser;
    private Product testProduct;
    private CartItem testCartItem;

//    private static User setupTestUser() {
//        User _testUser;
//    }

    @Before
    public void setUp() throws Exception {
         testUser = new User(
                "lindell",
                "password",
                "email@email.com",
                ""
        );
        testUser.setUserid(1);

        testProduct = new Product();
        testProduct.setProductid(11);
        testProduct.setName("Test Product 1");
        testProduct.setPrice(100.0);
        testProduct.setDescription("Test Product Description 1");

        testCartItem = new CartItem(testUser,
                testProduct,
                10,
                "");

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listCartForCurrentUser() throws Exception {
        String apiUrl = "/carts";
        Mockito.when(securityContextService.getCurrentUserDetails())
                .thenReturn(testUser);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(testUser);

        assertEquals(er, tr);
    }

    @Test
    public void addToCart() throws Exception {
        String apiUrl = "/carts/add/product/{productid}";
        Mockito.when(securityContextService.getCurrentUserDetails())
                .thenReturn(testUser);
        Mockito.when(cartItemService.addToCart(1L, 1L, ""))
                .thenReturn(testCartItem);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl,
                1L)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(testCartItem);

        assertEquals(er, tr);
    }

    @Test
    public void removeFromCart() throws Exception {
        String apiUrl = "/carts/remove/product/{productid}";
        Mockito.when(securityContextService.getCurrentUserDetails())
                .thenReturn(testUser);
        Mockito.when(cartItemService.removeFromCart(1L, 1L, ""))
                .thenReturn(testCartItem);

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl,
                1L)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();

        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(testCartItem);

        assertEquals(er, tr);
    }
}