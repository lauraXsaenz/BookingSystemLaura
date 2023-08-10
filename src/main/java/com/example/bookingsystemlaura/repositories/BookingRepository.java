package com.example.bookingsystemlaura.repositories;

import com.example.bookingsystemlaura.entities.Booking;
import com.example.bookingsystemlaura.entities.SalesMan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    //List<Booking> findStudentById(long kw);
}
