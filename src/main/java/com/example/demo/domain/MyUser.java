package com.example.demo.domain;

import java.util.List;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;

//@Data
@Entity
@Table(
        name = "users1",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"username", "email"})  // Уникальная пара
        }
)
public class MyUser {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private Long id;

        //@Column(nullable = false)
        private String username;

        //@Column(nullable = false)
        private String email;

        //@Column(nullable = false)
        private String password;

        //@Column(nullable = false)
        private String role;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
        private List<Booking> bookings;

//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    public enum Role { USER, ADMIN }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }


}
