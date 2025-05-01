package com.example.demo.controller;

import com.example.demo.domain.*;
import com.example.demo.repos.AccommodationRepository;
import com.example.demo.repos.BookingRepository;
import com.example.demo.repos.UserRepo;
import com.example.demo.services.BookingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private AccommodationRepository accommodationRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @PostMapping("/create")
    public String createBooking(
            @RequestParam Long accommodationId,
            @RequestParam Long userId,
            @RequestParam int guestsCount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            RedirectAttributes redirectAttributes) {

        try {
            Accommodation accommodation = accommodationRepository.findById(accommodationId)
                    .orElseThrow(() -> new EntityNotFoundException("Размещение не найдено"));

            MyUser user = userRepo.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

            // Расчет количества дней и стоимости
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            BigDecimal totalPrice = accommodation.getBasePrice()
                    .multiply(BigDecimal.valueOf(days))
                    .multiply(BigDecimal.valueOf(guestsCount));

            Booking booking = new Booking();
            booking.setAccommodation(accommodation);
            booking.setUser(user);
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setGuestsCount(guestsCount);
            booking.setTotalPrice(totalPrice);
            booking.setStatus(BookingStatus.ACTIVE);

            bookingService.save(booking);

            redirectAttributes.addFlashAttribute("success", "Бронирование успешно создано!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании бронирования: " + e.getMessage());
        }

        return "redirect:/";
    }


    @GetMapping("/history")
    public String showHistory(Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Для администратора - все бронирования
            List<BookingHistoryDto> allBookings = bookingRepository.findAllBookingHistory();
            model.addAttribute("bookings", allBookings);
        } else {
            // Для обычного пользователя - только его бронирования
            MyUser user = userRepo.findByUsername(principal.getName());
            List<BookingHistoryDto> userBookings = bookingRepository.findByUserId(user.getId());
            model.addAttribute("bookings", userBookings);
        }

        return "history";
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBookingStats() {
        // Получаем статистику (дата заезда -> количество бронирований)
        Map<LocalDate, Long> stats = bookingService.getBookingsCountByStartDate();

        // Преобразуем в формат для Chart.js
        List<String> dates = stats.keySet().stream()
                .sorted()
                .map(LocalDate::toString)
                .collect(Collectors.toList());

        List<Long> counts = stats.keySet().stream()
                .sorted()
                .map(stats::get)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("dates", dates);
        response.put("counts", counts);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        boolean canceled = bookingService.cancelBooking(bookingId);
        if (canceled) {
            redirectAttributes.addFlashAttribute("success", "Бронирование успешно отменено!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Ошибка при отмене бронирования.");
        }
        return "redirect:/bookings/history";
    }

    @PostMapping("/restore/{bookingId}")
    public String restoreBooking(@PathVariable Long bookingId, RedirectAttributes redirectAttributes) {
        boolean restored = bookingService.restoreBooking(bookingId);
        if (restored) {
            redirectAttributes.addFlashAttribute("success", "Бронирование успешно восстановлено!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Ошибка при восстановлении бронирования.");
        }
        return "redirect:/bookings/history";
    }
}