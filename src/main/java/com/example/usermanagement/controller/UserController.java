package com.example.usermanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.dto.UserRequestDTO;
import com.example.usermanagement.dto.UserResponseDTO;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService uservice;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public UserController(UserService uservice) {
        this.uservice = uservice;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = uservice.getAllUsers();

        List<UserResponseDTO> userResponseDTOs = users.stream()
                .map(userMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(userResponseDTOs);
    }

    @GetMapping("/paginated")
    public ResponseEntity<List<UserResponseDTO>> getUserWithPagination(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Page<User> users = uservice.getUsersWithPagination(page, size, sortBy, direction);
        List<UserResponseDTO> res = users.stream().map(userMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    // get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        User user = uservice.getUserById(id);
        UserResponseDTO res = userMapper.toDto(user);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request) {
        User user = userMapper.toEntity(request);
        User savedUser = uservice.createUser(user);
        UserResponseDTO res = userMapper.toDto(savedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserRequestDTO updatedUser) {
        User user = userMapper.toEntity(updatedUser);
        User updated = uservice.updateUser(id, user);
        UserResponseDTO res = userMapper.toDto(updated);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        uservice.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchByName(@RequestParam String keyword) {
        return ResponseEntity.ok(uservice.searchByName(keyword));
    }
}
