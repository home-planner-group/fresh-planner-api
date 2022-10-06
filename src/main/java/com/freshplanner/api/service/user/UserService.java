package com.freshplanner.api.service.user;

import com.freshplanner.api.exception.ElementNotFoundException;

public interface UserService {
    UserEntity getUserByName(String username) throws ElementNotFoundException;
}
