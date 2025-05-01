package com.example.demo.repos;

import com.example.demo.domain.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("SELECT p FROM Accommodation p WHERE CONCAT(p.name, ' ', p.type, ' ', p.location, ' ', p.capacity, ' ', p.basePrice, ' ', p.description) LIKE %?1%")
    List<Accommodation> search(String keyword);
}