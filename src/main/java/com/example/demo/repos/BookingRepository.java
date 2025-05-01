package com.example.demo.repos;

import com.example.demo.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{
    @Query("""
    SELECT 
        b.id as bookingId,
        u.username as username,
        u.email as email,
        a.name as accommodationName,
        a.type as accommodationType,
        a.location as accommodationLocation,
        b.status as status,
        b.startDate as startDate,
        b.endDate as endDate,
        b.guestsCount as guestsCount,
        b.totalPrice as totalPrice
    FROM Booking b
    JOIN b.user u
    JOIN b.accommodation a
    ORDER BY b.startDate DESC
    """)
    List<BookingHistoryDto> findAllBookingHistory();

    @Query("""
    SELECT 
        b.id as bookingId,
        u.username as username,
        u.email as email,
        a.name as accommodationName,
        a.type as accommodationType,
        a.location as accommodationLocation,
        b.status as status,
        b.startDate as startDate,
        b.endDate as endDate,
        b.guestsCount as guestsCount,
        b.totalPrice as totalPrice
    FROM Booking b
    JOIN b.user u
    JOIN b.accommodation a
    WHERE u.id = :userId
    ORDER BY b.startDate DESC
    """)
    List<BookingHistoryDto> findByUserId(@Param("userId") Long userId);


    List<Booking> findByUser(MyUser user);
    List<Booking> findAll();
    Optional<Booking> findById(Long bookingId);
}