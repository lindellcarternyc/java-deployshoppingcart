package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service(value = "securityContextService")
public class SecurityContextServiceImpl implements SecurityContextService {
    @Autowired
    UserService userService;

    @Override
    public User getCurrentUserDetails() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return userService.findByName(username);
    }
}
