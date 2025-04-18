package com.example.demo.repos;
import com.example.demo.domain.MyUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);
    MyUser findByUsername(String username);

    List<MyUser> findByUsernameContainingOrEmailContaining(String username, String email);

    boolean existsByUsernameAndEmail(String username, String email);
}
