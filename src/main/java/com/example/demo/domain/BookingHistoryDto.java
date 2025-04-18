package com.example.demo.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BookingHistoryDto {
    Long getBookingId();
    String getUsername();
    String getEmail();
    String getAccommodationName();
    String getAccommodationType();
    String getAccommodationLocation();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Integer getGuestsCount();
    BigDecimal getTotalPrice();

}