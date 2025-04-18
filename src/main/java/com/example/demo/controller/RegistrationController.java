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
//    @Autowired
//    private JdbcUserDetailsManager userDetailsManager;

//    @Autowired
//    private UserService userService;

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

        // Проверяем уникальность пары (username, email)
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
//    @PostMapping("/registration")
//    public String registerUser(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam String role) {
////        // Проверяем, существует ли пользователь с таким именем
////        if (userRepository.findByUsername(username).isPresent()) {
////            return "redirect:/login"; // Пользователь уже существует
////        }
//
//        // Создаем нового пользователя
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password)); // Шифруем пароль
//        user.setRole(role);
//
//        // Сохраняем пользователя в базе данных
//        userRepository.save(user);
//
//        return "redirect:/login"; // Перенаправляем на страницу входа
//    }
//}

//    @PostMapping(value = "/save_user")
//    public String saveUser(@ModelAttribute("user") User user) {
//        userService.save(user);
//        return "login/";
//    }


//@Controller
//public class RegistrationController {
//
//    private final UserService userService;
//
//    @GetMapping("/registration")
//    public String registration(Model model) {
//        model.addAttribute("userForm", new User());
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }
//        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
//            model.addAttribute("passwordError", "Пароли не совпадают");
//            return "registration";
//        }
//        if (!userService.saveUser(userForm)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
//            return "registration";
//        }
//
//        return "redirect:/";
//    }
//}

//@Controller
//public class RegistrationController {
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/registration")
//    public String registration(){
//        return "registration";
//    }
//
//    @PostMapping("/registration")
//    public String addUser(@RequestBody MyUser user) {
//        userService.addUser(user);
//        return "redirect:/login";
//        try {
//            userService.registerUser(user); // Регистрируем пользователя через сервис
//            return "redirect:/login"; // Перенаправляем на страницу входа
//        } catch (IllegalArgumentException e) {
//            model.addAttribute("message", e.getMessage()); // Сообщение об ошибке
//            return "registration"; // Возвращаем страницу регистрации с ошибкой
//        }
//    }
//}



