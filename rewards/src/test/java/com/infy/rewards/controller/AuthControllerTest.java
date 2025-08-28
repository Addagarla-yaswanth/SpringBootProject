package com.infy.rewards.controller;

import com.infy.rewards.config.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testGenerateToken_Success() throws Exception {
        // Mock successful authentication
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("Yaswanth", "12345")
        )).thenReturn(null);

        // Mock JWT token
        Mockito.when(jwtUtil.generateToken("Yaswanth")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/customer/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"custName\":\"Yaswanth\", \"phoneNo\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("fake-jwt-token"));
    }
}
