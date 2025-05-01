package com.example.demo.controller;
import com.example.demo.domain.MyUser;
import com.example.demo.repos.UserRepo;
//import com.example.demo.domain.Role;


//import com.example.demo.services.UserService;
//import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("users", new MyUser());
        return "registration";
    }

    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("users") MyUser user, BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }

        if (userRepository.existsByUsernameAndEmail(user.getUsername(), user.getEmail())) {
            // Добавляем глобальную ошибку (не привязанную к конкретному полю)
            result.reject("user.exists", "Пользователь с таким именем и почтой уже существует");
            return "registration";
        }
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole());
        userRepository.save(user);
        return "redirect:/login";
    }
}
