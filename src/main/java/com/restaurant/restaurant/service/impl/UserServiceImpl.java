package com.restaurant.restaurant.service.impl;

import com.restaurant.restaurant.config.JwtUtil;
import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.UserDTO;
import com.restaurant.restaurant.dto.enums.Role;
import com.restaurant.restaurant.entity.User;
import com.restaurant.restaurant.exception.CustomException;
import com.restaurant.restaurant.exception.ResourceNotFoundException;
import com.restaurant.restaurant.repository.UserRepository;
import com.restaurant.restaurant.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JwtUtil jwtUtil;

    @Override
    @Transactional
    public void registerUser(UserDTO userDTO) {
        logger.info("Registering user: {}", userDTO.getUsername());

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            logger.error("Username '{}' is already taken", userDTO.getUsername());
            throw new CustomException("Username already taken!");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.valueOf(userDTO.getRole()));

        userRepository.save(user);
        logger.info("User '{}' registered successfully with role '{}'", user.getUsername(), user.getRole());
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        logger.info("Attempting login for user: {}", authRequest.getUsername());

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> {
                    logger.warn("User '{}' not found", authRequest.getUsername());
                    return new ResourceNotFoundException("User not found!");
                });

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        String token = jwtUtil.generateToken(user.getUsername());
        logger.info("User '{}' logged in successfully, JWT generated", authRequest.getUsername());

        return new AuthResponse(token);
    }

}
