package com.example.demo.services;

import com.example.demo.domain.Accommodation;
import com.example.demo.domain.Booking;
import com.example.demo.domain.MyUser;
import com.example.demo.repos.AccommodationRepository;
import com.example.demo.repos.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AccommodationService {

    @Autowired
    private AccommodationRepository accommodationRepository;

    public List<Accommodation> listAll(String keyword){
        if (keyword != null){
            return accommodationRepository.search(keyword);
        }
        return accommodationRepository.findAll();
    }

    public void save(Accommodation accommodation){accommodationRepository.save(accommodation);}

    public Accommodation get(Long id){ ///редактировнание
        return accommodationRepository.findById(id).get();
    }

    public void delete(Long id){
        accommodationRepository.deleteById(id);
    }
}
