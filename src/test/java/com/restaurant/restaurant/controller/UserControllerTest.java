package com.restaurant.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.restaurant.dto.AuthRequest;
import com.restaurant.restaurant.dto.AuthResponse;
import com.restaurant.restaurant.dto.UserDTO;
import com.restaurant.restaurant.exception.ResourceNotFoundException;
import com.restaurant.restaurant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private AuthRequest authRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest("testuser", "password123");
        authResponse = new AuthResponse("mocked-jwt-token");
    }

    @Test
    @WithMockUser
    void testRegister_Success() throws Exception {
        UserDTO userDTO = new UserDTO("testuser", "test@example.com", "password123");

        // Mock the service method to do nothing
        doNothing().when(userService).registerUser(any(UserDTO.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User registered successfully."));
    }

    @Test
    @WithMockUser
    void testLogin_Success() throws Exception {
        // Mock the service method to return a valid response
        when(userService.login(any(AuthRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mocked-jwt-token"));
    }

    @Test
    @WithMockUser
    void testLogin_UserNotFound() throws Exception {
        when(userService.login(any(AuthRequest.class)))
                .thenThrow(new ResourceNotFoundException("User not found!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found!"));
    }

}
