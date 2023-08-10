package com.example.bookingsystemlaura.web;

import com.example.bookingsystemlaura.entities.Booking;
import com.example.bookingsystemlaura.entities.Seat;
import com.example.bookingsystemlaura.repositories.BookingRepository;
import com.example.bookingsystemlaura.repositories.SalesManRepository;
import com.example.bookingsystemlaura.repositories.SeatRepository;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@SessionAttributes({"currentSeat"})
@Controller
@AllArgsConstructor
public class BookingController {




    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SalesManRepository salesManRepository;
    @Autowired
    private SeatRepository seatRepository;



    @GetMapping("/home")
    public String showHome(Model model) {
        List<Seat> theSeats = seatRepository.findAll();
        model.addAttribute("seats", theSeats);


        return "home";
    }


    @PostMapping("/saveBooking")
    @Transactional
    public String saveBooking(Model model, @RequestParam("seatId") Long seatId, @RequestParam("selectedName") String selectedName, @RequestParam("customerName") String customerName, HttpSession session) throws ParseException {

       // Long currentSeat = (Long) session.getAttribute("currentSeat");
            String desiredPattern = "yyyy-MM-dd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(desiredPattern);
            Date currentDate = new Date();
            String formattedDate = dateFormat.format(currentDate);
            Date parsedDate = dateFormat.parse(formattedDate);
            Optional<Seat> seat = seatRepository.findById(seatId); // preguntarle a andre por esto
            Booking booking = new Booking(null, customerName,  seat.get().getSeatNum(),  seat.get().getSeatLetter(), currentDate,selectedName);
            bookingRepository.save(booking);
            seatRepository.updateSeatStatus(seatId, true);
            System.out.println("Seat ID: " + seatId);
            System.out.println("Selected Name: " + selectedName);
            System.out.println("Customer Name: " + customerName);
            return "redirect:/bookings";
        }



    @GetMapping("/bookings")
    public String showBookings(Model model) {
    List<Booking> theBookings= bookingRepository.findAll();
    model.addAttribute("bookings", theBookings);

        return "bookings";
    }


    //editBooking
    @GetMapping("/editBooking")
    public String editBookings (ModelMap model, Long id,
                                HttpSession session){



    Booking booking = bookingRepository.findById(id).orElse(null);
       if(booking==null) throw new RuntimeException("Booking does not exist");

        Optional<Seat> optionalSeat = seatRepository.findBySeatNumAndSeatLetter(booking.getSeatNum(), booking.getSeatLetter());

        System.out.println("this is the id " + optionalSeat.map(Seat::getId).orElse(null));

       List<Seat> available =seatRepository.findByStatus(false);

       model.addAttribute("booking", booking);
       model.addAttribute("available", available);




        return "editBooking";
    }

    //Este método se encarga de editar una reserva existente y también de gestionar la asignación y liberación de asientos relacionados con la reserva.
    @PostMapping("/editBooking")
    @Transactional // toca ponerlo porque voy a hacer uso de la query que defini en seatRepository
    public String editBooking(@RequestParam("bookingId") Long bookingId, @RequestParam("seatId") Long seatId, @RequestParam("selectedName") String selectedName, @RequestParam("customerName") String customerName) {
        // Obtain the existing booking by its ID
        Booking existingBooking = bookingRepository.findById(bookingId).orElse(null);
        if (existingBooking != null) {
            // Get the original seat associated with the booking before the edit
            Seat originalSeat = seatRepository.findBySeatNumAndSeatLetter(
                    existingBooking.getSeatNum(),
                    existingBooking.getSeatLetter()
            ).orElse(null);

            if (originalSeat != null) {
                // Release the original seat (update its status to "false")
                seatRepository.updateSeatStatus(originalSeat.getId(), false);
            }

            // Update the existing booking with the new values
            existingBooking.setCustomerName(customerName);
            existingBooking.setSalesManName(selectedName);

            // Get the new selected seat and update its status
            Seat newSeat = seatRepository.findById(seatId).orElse(null);
            if (newSeat != null) {
                existingBooking.setSeatNum(newSeat.getSeatNum());
                existingBooking.setSeatLetter(newSeat.getSeatLetter());
                seatRepository.updateSeatStatus(newSeat.getId(), true);
            }

            // Save the updated existing booking
            bookingRepository.save(existingBooking);
        }
        return "redirect:/bookings";
    }


    @PostMapping(path = "/save")
    public String save(Model model, Booking booking, BindingResult bindingResult, ModelMap mm, HttpSession session) {

            bookingRepository.save(booking);

            return "redirect:home";

    }


    @GetMapping("/delete")
    @Transactional
    public String delete(Long id) {
        Optional<Booking> bookingToDelete = bookingRepository.findById(id); // El resultado se almacena en un Optional, lo que significa que podría o no contener una reserva, dependiendo de si se encuentra o no.

        if (bookingToDelete.isPresent()) {
            Booking deletedBooking = bookingToDelete.get();

            // Get the associated seat
            Seat seatToRelease = seatRepository.findBySeatNumAndSeatLetter(
                    deletedBooking.getSeatNum(),
                    deletedBooking.getSeatLetter()
            ).orElse(null);

            if (seatToRelease != null) {
                // Release the seat (update its status to "false")
                seatRepository.updateSeatStatus(seatToRelease.getId(), false);
            }

            // Delete the booking from the database
            bookingRepository.deleteById(id);
        }

        return "redirect:/bookings";
    }

}