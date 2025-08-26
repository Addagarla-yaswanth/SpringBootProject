package com.infy.rewards.controller;

import com.infy.rewards.config.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getCustName(), authRequest.getPhoneNo())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Invalid username or password");
        }

        return jwtUtil.generateToken(authRequest.getCustName());
    }

    @Data
    public static class AuthRequest {
        private String custName;
        private String phoneNo;
    }
}
