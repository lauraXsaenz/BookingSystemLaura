package com.example.bookingsystemlaura.web;

import com.example.bookingsystemlaura.entities.Booking;
import com.example.bookingsystemlaura.entities.Seat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class BookingControllerTest {

Booking booking;
Seat seat;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void showHome() {
    }

    @Test
    void saveBooking() {
    }

    @Test
    void showBookings() {
    }

    @Test
    void editBookings() {
    }

    @Test
    void editBooking() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }
}