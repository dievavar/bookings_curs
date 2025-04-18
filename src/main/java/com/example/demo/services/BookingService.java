package com.example.demo.services;

import com.example.demo.domain.Accommodation;
import com.example.demo.domain.Booking;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.domain.MyUser;
import com.example.demo.repos.AccommodationRepository;
import com.example.demo.repos.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired; //для связи всех зависимомтей из всех классов
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service; //для обнаружения всех зависимостей и согласно нашей(3ех звенной) арх. указываем что StudentService - сервер(хранится бизнес логика)
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public class BookingService {
    @Autowired
    private BookingRepository repo;

    @Autowired
    private AccommodationRepository accommodationRepository;

//    public List<Booking> listAll(String keyword){
//        if (keyword != null){
//            return repo.search(keyword);
//        }
//        return repo.findAll();
//    }

    public void save(Booking booking){repo.save(booking);}

    public Booking get(Long id){ ///редактировнание
        return repo.findById(id).get();
    }

    public void delete(Long id){
        repo.deleteById(id);
    }





    public Booking createBooking(Long accommodationId, LocalDate startDate,
                                 LocalDate endDate, MyUser user) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found"));

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal totalPrice = accommodation.getBasePrice().multiply(BigDecimal.valueOf(days));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setAccommodation(accommodation);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalPrice(totalPrice);
//        booking.setStatus(Booking.BookingStatus.PENDING);

        return repo.save(booking);
    }

    public List<Booking> getAllBookings() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "bookingTime"));
    }

    public List<Booking> getUserBookings(MyUser user) {
        return repo.findByUser(user);
    }

    public void confirmBooking(Long bookingId) {
        Booking booking = repo.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
//        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        repo.save(booking);
    }

    public Map<LocalDate, Long> getBookingsCountByStartDate() {
        return repo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Booking::getStartDate,
                        Collectors.counting()
                ));
    }
    @Transactional
    public void cancelBooking(Long id, String username, String adminNote) {
        Booking booking = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));

        // Проверка прав (пользователь может отменять только свои бронирования)
        if (!booking.getUser().getEmail().equals(username) && !isAdmin()) {
            throw new AccessDeniedException("Нет прав для отмены этого бронирования");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        if (adminNote != null) {
            booking.setAdminNote(adminNote);
        }
        repo.save(booking);
    }

    @Transactional
    public void restoreBooking(Long id) {
        Booking booking = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Бронирование не найдено"));

        booking.setStatus(Booking.BookingStatus.ACTIVE);
        repo.save(booking);
    }


}
