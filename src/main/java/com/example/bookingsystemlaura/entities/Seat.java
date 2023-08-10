package com.example.bookingsystemlaura.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int seatNum;
    private String seatLetter;
    private double price;
    private boolean status;


}
