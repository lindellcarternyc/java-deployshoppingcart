package com.lambdaschool.shoppingcart.controllers;

import com.lambdaschool.shoppingcart.models.CartItem;
import com.lambdaschool.shoppingcart.models.User;
import com.lambdaschool.shoppingcart.services.CartItemService;
import com.lambdaschool.shoppingcart.services.SecurityContextService;
import com.lambdaschool.shoppingcart.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityContextService securityContextService;

    @GetMapping(value = "",
        produces = {"application/json"})
    public ResponseEntity<?> listCartForCurrentUser()
    {
        User user = securityContextService.getCurrentUserDetails();
        return new ResponseEntity<>(user,
            HttpStatus.OK);
    }

    @PutMapping(value = "/add/product/{productid}",
        produces = {"application/json"})
    public ResponseEntity<?> addToCart(@PathVariable long productid)
    {
        long userId = securityContextService.getCurrentUserDetails().getUserid();

        CartItem addCartItem = cartItemService.addToCart(userId,
            productid, "");
        return new ResponseEntity<>(addCartItem,
            HttpStatus.OK);
    }

    @DeleteMapping(value = "/remove/product/{productid}",
        produces = {"application/json"})
    public ResponseEntity<?> removeFromCart(@PathVariable long productid)
    {
        long userId = securityContextService.getCurrentUserDetails().getUserid();

        CartItem removeCartItem = cartItemService.removeFromCart(userId,
            productid,
            "");
        return new ResponseEntity<>(removeCartItem,
            HttpStatus.OK);
    }
}
