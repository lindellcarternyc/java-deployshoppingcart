package com.lambdaschool.shoppingcart.services;

import com.lambdaschool.shoppingcart.models.User;

public interface SecurityContextService {
    User getCurrentUserDetails();
}
