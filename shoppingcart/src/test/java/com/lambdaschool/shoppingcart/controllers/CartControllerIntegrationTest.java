package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.ShoppingCartApplicationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = ShoppingCartApplicationTest.class)
@AutoConfigureMockMvc
@WithUserDetails(value = "test-barnbarn")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private boolean ranAddTest = false;

    @Before
    public void setUp() throws Exception {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    @Order(1)
    public void listCartForCurrentUser() {
        given().when()
                .get("/carts")
                .then()
                .statusCode(200)
                .and()
                .body(containsString("test-barnbarn"));
    }

    @Test
    @Order(2)
    public void addToCart() {
        ranAddTest = true;
        given().when()
                .put("/carts/add/product/1")
                .then()
                .statusCode(200)
                .and()
                .body(containsString("\"quantity\":5"));
    }

    @Test
    @Order(3)
    public void removeFromCart() {
        given().when()
                .delete("/carts/remove/product/{productid}", 1)
                .then()
                .statusCode(200)
                .body(containsString("\"quantity\":3"));
    }
}