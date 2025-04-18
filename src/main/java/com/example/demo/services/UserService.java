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
public class UserService implements UserDetailsService{
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

    public MyUser get(Long id){ ///редактировнание
        return userRepository.findById(id).get();
    }

    public void delete(Long id){userRepository.deleteById(id);
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
//    @Override
//    public void save(User user){
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setRole(user.getRole());
//        userRepository.save(user);
//    }
//    @Override
//    public User findByUsername(String username){
//        return userRepository.findByUsername(username);
//    }
//        // Загружаем пользователя из базы данных
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));
//
//        // Возвращаем UserDetails, который Spring Security использует для аутентификации
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole()) // Убедитесь, что роль имеет префикс "ROLE_"
//                .build();


}




////    @Autowired
////    private  RoleRepo roleRepository;
//    @Autowired
//    private  PasswordEncoder passwordEncoder;
//
////    public UserService(UserRepo userRepository, RoleRepo roleRepository, PasswordEncoder passwordEncoder) {
////        this.userRepository = userRepository;
////        this.roleRepository = roleRepository;
////        this.passwordEncoder = passwordEncoder;
////    }
//
//    public void registerUser(String username, String password, String role) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setRole(role);
//        userRepository.save(user);
//    }


//@Service
//public class UserService implements UserDetailsService {
//    @PersistenceContext
//    private EntityManager em;
//    @Autowired
//    UserRepo userRepo;
//    @Autowired
//    RoleRepo roleRepo;
//    @Autowired
//    BCryptPasswordEncoder ;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByUsername(username).get();
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        return user;
//    }
//
//    public User findUserById(Long userId) {
//        Optional<User> userFromDb = userRepo.findById(userId);
//        return userFromDb.orElse(new User());
//    }
//
//    public List<User> allUsers() {
//        return userRepo.findAll();
//    }
//
//    public boolean saveUser(User user) {
//        User userFromDB = userRepo.findByUsername(user.getUsername()).get();
//
//        if (userFromDB != null) {
//            return false;
//        }
//
//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        userRepo.save(user);
//        return true;
//    }
//
//    public boolean deleteUser(Long userId) {
//        if (userRepo.findById(userId).isPresent()) {
//            userRepo.deleteById(userId);
//            return true;
//        }
//        return false;
//    }
//
//    public List<User> usergtList(Long idMin) {
//        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
//                .setParameter("paramId", idMin).getResultList();
//    }
//}
//
//@Service
//public class UserService implements UserDetailsService{
//    @Autowired
//    private UserRepo repository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<MyUser> user = repository.findByName((username));
//        return user.map(MyUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
//    }
//    public void addUser(MyUser user){
//        repository.save(user);
//    }
//
//
//    //    public void registerUser(User user) {
////        User userFromDb = userRepo.findByUsername(user.getUsername());
////
////        if (userFromDb != null) {
////            throw new IllegalArgumentException("User already exists");
////        }
////
////        user.setActive(true);
////        user.setRoles(Collections.singleton(Role.USER));
////        user.setPassword((user.getPassword())); // Кодируем пароль
////        userRepo.save(user);
////    }
//}
//
