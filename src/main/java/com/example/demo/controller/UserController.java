package com.example.demo.controller;

import com.example.demo.domain.MyUser;

import java.security.Principal;
import java.util.*;

import com.example.demo.services.BookingService;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller; // для управляющего класса
import org.springframework.ui.Model; //интерфейс для взаимодействия колнтроолера и конфигуратора и для добавления элементов в веб интерфейс
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView; //метод для указания html страниц которые подвязаны  с контроллером

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookingService bookingService;

    //контроллер по конролю пользоваетелей для администратора
    @RequestMapping
    public String manageUsers(Model model, @Param("myUsers") String myUsers, Principal principal) {
         //Получаем текущего пользователя
        String currentUsername = principal.getName();
        model.addAttribute("currentUsername", currentUsername);

        List<MyUser> listUsers = userService.listAll(myUsers);
        model.addAttribute("listSUsers", listUsers);
        model.addAttribute("myUsers", myUsers);
        return "manage_users.html"; // Имя шаблона Thymeleaf
    }

//    @RequestMapping("/users/new") //контроллер по добавлению пользователя
//    public String showNewUserForm(Model model){
//        MyUser user = new MyUser();
//        model.addAttribute("user", user);
//        return "redirect:/users";
//    }
    @PostMapping("/new")
    public String addUser(@RequestParam String username,
                          @RequestParam String email,
                          @RequestParam String password,
                          @RequestParam String role) {
        // Создаем нового пользователя
        MyUser user = new MyUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Шифруем пароль
        user.setRole(role);

        // Сохраняем пользователя в базу данных
        userService.save(user);

        // Перенаправляем на страницу управления пользователями
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/save", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") MyUser user) {
        userService.save(user);
        return "redirect:/users";
    }

    @RequestMapping("/delete/{id}") //для удаления пользователей
    public String deleteUser(@PathVariable(name="id") Long id){
        userService.delete(id);
        return "redirect:/users";
    }

    @PostMapping("/updateRole")
    public String updateUserRole(@RequestParam Long id, @RequestParam String role) {
        userService.updateUserRole(id, role); // Обновляем роль пользователя
        return "redirect:/users"; // Перенаправляем обратно на страницу со списком пользователей
    }

    @GetMapping("/bookings")
    public String allBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "/users/bookings";
    }

    @PostMapping("/bookings/{id}/confirm")
    public String confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return "redirect:/users/bookings";
    }
}
