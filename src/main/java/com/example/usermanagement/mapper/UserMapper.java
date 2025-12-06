package com.example.usermanagement.mapper;

import org.springframework.stereotype.Component;

import com.example.usermanagement.dto.UserRequestDTO;
import com.example.usermanagement.dto.UserResponseDTO;
import com.example.usermanagement.model.User;

@Component
public class UserMapper {
    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return user;
    }

    public UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getAge(),
            user.getCreatedAt()
        );
    }
}
