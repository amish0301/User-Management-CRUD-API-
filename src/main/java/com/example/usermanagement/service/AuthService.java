package com.example.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.Enum.Role;
import com.example.usermanagement.dto.UserRequestDTO;
import com.example.usermanagement.dto.auth.AuthRequest;
import com.example.usermanagement.dto.auth.AuthResponse;
import com.example.usermanagement.dto.auth.RefreshTokenRequest;
import com.example.usermanagement.exception.DuplicateEmailException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.security.AuthJwtUtil;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthJwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    public AuthResponse register(UserRequestDTO request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = new User(request.getName(), request.getEmail(), request.getAge(),
                passwordEncoder.encode(request.getPassword()), Role.USER);
        userRepo.save(user);

        return new AuthResponse(null, null, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(AuthRequest reequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(reequest.getEmail(), reequest.getPassword()));

        UserDetails user = userRepo.findByEmail(reequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken, user.getUsername(),
                user.getAuthorities().iterator().next().getAuthority());
    }

    // refresh token
    public AuthResponse refreshToken(RefreshTokenRequest req) {
        String refreshToken = req.getRefreshToken();

        try {
            String email = jwtUtil.extractUserName(refreshToken);
            User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

            // validate refresh token
            if (!jwtUtil.validateToken(refreshToken, user)) {
                throw new RuntimeException("Invalid Refresh Token");
            }

            String newAccessToken = jwtUtil.generateAccessToken(user);
            return new AuthResponse(newAccessToken, refreshToken, user.getEmail(), user.getRole().name());
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh token: " + e.getMessage());
        }
    }

    public void logout(String email) {
        userRepo.findByEmail(email).ifPresent(user -> userRepo.delete(user));
    }
}
