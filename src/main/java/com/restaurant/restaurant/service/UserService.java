package com.restaurant.restaurant.service;

import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.UserDTO;

public interface UserService {
    void registerUser(UserDTO userDTO);

    AuthResponse login(AuthRequest authRequest);
}