package com.example.demo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
//@NonNull
//@Data
@Table(name="bookings1")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MyUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;


    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
;
    private int guestsCount;

    public enum BookingStatus {
        ACTIVE,    // Активное бронирование
        CANCELLED, // Отмененное
        COMPLETED  // Завершенное (после даты выезда)
    }

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.ACTIVE;


//    private String status;

//    @Column(name = "is_in_cart")
//    private boolean is_in_cart;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String type;
//
//    @Column(nullable = false)
//    private String location;
//
//    @Column(nullable = false)
//    private float price;
//
//    @NotNull(message = "Дата прибытия обязательна")
//    @FutureOrPresent(message = "Дата прибытия должна быть сегодня или позже")
//    @Column(name = "start_date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate start_date;
////
////    @NotNull(message = "Дата отправки обязательна")
////    @FutureOrPresent(message = "Дата отправки должна быть сегодня или позже")
//    @Column(name = "end_date")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate end_date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getGuestsCount() {
        return guestsCount;
    }

    public void setGuestsCount(int guestsCount) {
        this.guestsCount = guestsCount;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public float getPrice() {
//        return price;
//    }
//
//    public void setPrice(float price) {
//        this.price = price;
//    }
//
//    public LocalDate getStart_date() {
//        return start_date;
//    }
//
//    public void setStart_date(LocalDate start_date) {
//        this.start_date = start_date;
//    }
//
//    public LocalDate getEnd_date() {
//        return end_date;
//    }
//
//    public void setEnd_date(LocalDate end_date) {
//        this.end_date = end_date;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public MyUser getUser() {
//        return user;
//    }
//
//    public void setUser(MyUser user) {
//        this.user = user;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public boolean isIs_in_cart() {
//        return is_in_cart;
//    }
//
//    public void setIs_in_cart(boolean is_in_cart) {
//        this.is_in_cart = is_in_cart;
//    }
}
