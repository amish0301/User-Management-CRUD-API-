package com.example.usermanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.usermanagement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);
    public List<User> findByNameContaining(String keyword);
    public List<User> findByAgeGreaterThan(Integer age);
    public Page<User> findByNameContaining(String keyword, Pageable pageable);
}
