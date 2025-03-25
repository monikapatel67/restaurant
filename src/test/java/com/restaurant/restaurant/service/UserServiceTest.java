package com.restaurant.restaurant.service;

import com.restaurant.restaurant.config.JwtUtil;
import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.UserDTO;
import com.restaurant.restaurant.dto.enums.Role;
import com.restaurant.restaurant.entity.User;
import com.restaurant.restaurant.exception.CustomException;
import com.restaurant.restaurant.exception.ResourceNotFoundException;
import com.restaurant.restaurant.repository.UserRepository;
import com.restaurant.restaurant.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("testuser", "password", Role.USER.name());
        user = new User(1L, "testuser", "hashedPassword", Role.USER);
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);


        assertDoesNotThrow(() -> userService.registerUser(userDTO));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyExists() {
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> userService.registerUser(userDTO));
        assertEquals("Username already taken!", exception.getMessage());
    }

    @Test
    void login_Success() {
        AuthRequest authRequest = new AuthRequest("testuser", "password");

        when(userRepository.findByUsername(authRequest.getUsername())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getUsername())).thenReturn("fake-jwt-token");

        AuthResponse response = userService.login(authRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("fake-jwt-token", response.getToken());

        verify(userRepository, times(1)).findByUsername(authRequest.getUsername());
        verify(jwtUtil, times(1)).generateToken(user.getUsername());
    }

    @Test
    void login_UserNotFound() {
        AuthRequest authRequest = new AuthRequest("unknown", "password");
        when(userRepository.findByUsername(authRequest.getUsername())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.login(authRequest));
        assertEquals("User not found!", exception.getMessage());
    }
}
