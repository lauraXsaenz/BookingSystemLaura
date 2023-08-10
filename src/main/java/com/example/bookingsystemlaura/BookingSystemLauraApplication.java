package com.example.bookingsystemlaura;

import com.example.bookingsystemlaura.entities.Seat;
import com.example.bookingsystemlaura.repositories.SeatRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import java.util.Date;


@SpringBootApplication
public class BookingSystemLauraApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingSystemLauraApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner(SeatRepository seatRepository){
		return args -> {
			if (seatRepository.count() == 0) {
				char[] seatLetters = {'A', 'B', 'C', 'D', 'E'};
				int maxSeatNumber = 10;
				double minPrice = 100.0;
				double maxPrice = 200.0;

				for (char letter : seatLetters) {
					for (int num = 1; num <= maxSeatNumber; num++) {
						double price = minPrice + Math.random() * (maxPrice - minPrice);
						Seat seat = new Seat(null, num, String.valueOf(letter), price, false);
						seatRepository.save(seat);
					}
				}

				seatRepository.findAll().forEach(p -> {
					System.out.println(p.getSeatNum() + "" + p.getSeatLetter());
				});
			}
		};
	}

}
