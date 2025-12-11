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
import com.example.usermanagement.exception.DuplicateEmailException;
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
        String token = jwtUtil.generateToken(user);
        return new AuthResponse(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(AuthRequest reequest) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(reequest.getEmail(), reequest.getPassword()));

        UserDetails user = userRepo.findByEmail(reequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = jwtUtil.generateToken(user);

        return new AuthResponse(token, user.getUsername(), user.getAuthorities().iterator().next().getAuthority());
    }

}
