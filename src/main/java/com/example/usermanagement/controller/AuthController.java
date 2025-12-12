package com.example.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.dto.UserRequestDTO;
import com.example.usermanagement.dto.auth.AuthRequest;
import com.example.usermanagement.dto.auth.AuthResponse;
import com.example.usermanagement.dto.auth.RefreshTokenRequest;
import com.example.usermanagement.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequestDTO request) {
        AuthResponse respnse = authService.register(request);
        return ResponseEntity.ok(respnse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse res = authService.login(request);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth EndPoint is working!");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication) {
        String email = authentication.getName();
        authService.logout(email);
        return ResponseEntity.ok().body("Logout successful");
    } 
}
