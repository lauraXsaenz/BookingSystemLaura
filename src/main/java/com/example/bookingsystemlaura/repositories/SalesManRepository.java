package com.example.bookingsystemlaura.repositories;

import com.example.bookingsystemlaura.entities.SalesMan;
import com.example.bookingsystemlaura.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesManRepository extends JpaRepository<SalesMan,Long> {
}
