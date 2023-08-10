package com.example.bookingsystemlaura.repositories;

import com.example.bookingsystemlaura.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> {

List<Seat> findByStatus (boolean status);


    Optional<Seat> findBySeatNumAndSeatLetter(int seatNum, String seatLetter);


    @Modifying
    @Query("UPDATE Seat s SET s.status = :status WHERE s.id = :id")
    void updateSeatStatus(@Param("id") Long id, @Param("status") boolean status);
}
