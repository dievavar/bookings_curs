package com.example.demo.services;

//import com.example.demo.config.MyUserDetails;
//import com.example.demo.domain.Role;
import com.example.demo.domain.MyUser;
import com.example.demo.domain.MyUser;
import com.example.demo.repos.UserRepo;
//import com.example.demo.repos.RoleRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }

    public List<MyUser> listAll(String keyword) {
        if (keyword != null) {
            return userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);
        }
        return userRepository.findAll();
    }


//    public List<MyUser> listAll(String keyword){
//        if (keyword != null){
//            return (List<MyUser>)userRepository.findByEmail(keyword);
//        }
//        return userRepository.findAll();
//    }

//    public void save(MyUser user){userRepository.save(user);}

    public boolean save(MyUser user) {
        // Проверяем, есть ли уже такой username + email
        if (userRepository.existsByUsernameAndEmail(user.getUsername(), user.getEmail())) {
            return false; // Пара уже существует
        }

        userRepository.save(user);
        return true;
    }

    public MyUser get(Long id) { ///редактировнание
        return userRepository.findById(id).get();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUserRole(Long id, String role) {
        MyUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        user.setRole(role); // Устанавливаем новую роль
        userRepository.save(user); // Сохраняем изменения
    }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        String username = authentication.getName();
        MyUser user = userRepository.findByUsername(username);

        return user.getId();
    }
}