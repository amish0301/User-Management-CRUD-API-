package com.example.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.usermanagement.model.User;
import com.example.usermanagement.service.UserService;

@Controller
public class WebController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        List<User> users = userService.getAllUsers();
        System.err.println("Users : " + users);

        int totalUsers = users.size();
        double avgAge = users.isEmpty() ? 0 : users.stream().mapToInt(User::getAge).average().orElse(0);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("avgAge", String.format("%.0f", avgAge));
        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping("/health")
    public String health(Model model) {
        model.addAttribute("status", "OK");
        model.addAttribute("message", "Application is running!");
        return "health"; 
    }
}
