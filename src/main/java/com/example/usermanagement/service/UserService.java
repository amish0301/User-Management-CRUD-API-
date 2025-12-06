package com.example.usermanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.usermanagement.exception.DuplicateEmailException;
import com.example.usermanagement.exception.UserNotFoundException;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get User with pagination
    public Page<User> getUsersWithPagination(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findAll(pageable);

    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    // create user
    public User createUser(User user) {
        // if already exists user
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(user.getEmail());
        }
        return userRepository.save(user);
    }

    // update user
    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        // if new email already taken
        userRepository.findByEmail(updatedUser.getEmail()).ifPresent(existingUser -> {
            // email exist but id is different
            if (!existingUser.getId().equals(id)) {
                throw new DuplicateEmailException(updatedUser.getEmail());
            }
        });

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    // search user by name
    public List<User> searchByName(String keyword) {
        return userRepository.findByNameContaining(keyword);
    }
}
