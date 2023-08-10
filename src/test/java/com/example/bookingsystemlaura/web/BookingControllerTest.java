package com.example.bookingsystemlaura.web;

import com.example.bookingsystemlaura.entities.Booking;
import com.example.bookingsystemlaura.entities.Seat;
import com.example.bookingsystemlaura.repositories.BookingRepository;
import com.example.bookingsystemlaura.repositories.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class BookingControllerTest {

Booking booking;
Seat seat;
    private MockMvc mockMvc;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    SeatRepository seatRepository;

    @Mock
    View mockView;

    @InjectMocks
    BookingController bookingController;

    @BeforeEach
    void setUp() throws ParseException {

    booking = new Booking();
    booking.setId(1L);
    booking.setCustomerName("Laura");
    booking.setSalesManName("Carlos");
    booking.setSeatNum(1);
    booking.setSeatLetter("A");
        String sDate1= "2012/11/11";
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
    booking.setBookingDate(date1);

    seat = new Seat();
    seat.setId(1L);
    seat.setSeatNum(1);
    seat.setSeatLetter("A");
    seat.setPrice(100);
    seat.setStatus(false);

    MockitoAnnotations.openMocks(this);

    mockMvc= MockMvcBuilders.standaloneSetup(bookingController).setSingleView(mockView).build();

    }


    @Test
    void showHome() throws Exception {

        List<Seat> theSeats = new ArrayList<Seat>();
        theSeats.add(seat);

        when(seatRepository.findAll()).thenReturn(theSeats);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("seats", theSeats))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("seats", hasSize(1)));
        verify(seatRepository, times(1)).findAll();
        verifyNoMoreInteractions(seatRepository);

    }

    @Test
    void saveBooking() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingRepository.save(booking);
        verify(bookingRepository, times (1)).save(booking);


    }

    @Test
    void testPostHome() throws Exception {

        Long seatId = 1L;
        String selectedName = "Selected Name";
        String customerName = "Customer Name";
        Seat seat = new Seat();
        seat.setId(seatId); // Set other seat properties as needed
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));

        mockMvc.perform(post("/saveBooking")
                        .param("seatId", "1")
                        .param("selectedName", "Selected Name")
                        .param("customerName", "Customer Name"))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    void showBookings() throws Exception {
        List<Booking> theBookings = new ArrayList<Booking>();
        theBookings.add(booking);

        when(bookingRepository.findAll()).thenReturn(theBookings);

        mockMvc.perform(get("/bookings"))


                .andExpect(status().isOk())
                .andExpect(model().attribute("bookings", theBookings))
                .andExpect(view().name("bookings"))
                .andExpect(model().attribute("bookings", hasSize(1)));
        verify(bookingRepository, times(1)).findAll();
        verifyNoMoreInteractions(bookingRepository);

    }

    @Test
    void editBookings1() {
    booking.setId(1L);
    seat.setId(1L);

    }
    @Test
    void updateSeat (){
        Long seatId = 1L;
        Seat seat = new Seat();
        seat.setId(seatId);

        seatRepository.updateSeatStatus(seat.getId(), false);
        verify(seatRepository, times(1)).updateSeatStatus(eq(seatId), eq(false));


// You might want to use ArgumentCaptor to capture the arguments and make more detailed verifications if needed
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> statusCaptor = ArgumentCaptor.forClass(Boolean.class);

        verify(seatRepository).updateSeatStatus(idCaptor.capture(), statusCaptor.capture());

// Retrieve the captured values
        Long capturedSeatId = idCaptor.getValue();
        Boolean capturedStatus = statusCaptor.getValue();

// Perform assertions to verify that the captured values are correct
        assertEquals(seatId, capturedSeatId);
        assertEquals(false, capturedStatus);
    }


    @Test
    void editBooking() {

        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        // ... Set other properties of existingBooking

        Seat originalSeat = new Seat();
        originalSeat.setId(1L);
        // ... Set other properties of originalSeat

        Seat newSeat = new Seat();
        newSeat.setId(2L);
        // ... Set other properties of newSeat

        // Mock repository methods
        when(seatRepository.findBySeatNumAndSeatLetter(existingBooking.getSeatNum(),existingBooking.getSeatLetter())).thenReturn(Optional.empty());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(existingBooking));
        when(seatRepository.findBySeatNumAndSeatLetter(anyInt(), anyString())).thenReturn(Optional.of(originalSeat));
        when(seatRepository.findById(anyLong())).thenReturn(Optional.of(newSeat));

        // Execute the method
        String result = bookingController.editBooking(1L, 2L, "SelectedName", "CustomerName");

        // Verify interactions
        verify(bookingRepository, times(1)).findById(anyLong());
        verify(seatRepository, times(1)).findBySeatNumAndSeatLetter(anyInt(), anyString());
        verify(seatRepository, times(1)).updateSeatStatus(anyLong(), eq(false));
        verify(seatRepository, times(1)).updateSeatStatus(anyLong(), eq(true));
        verify(bookingRepository, times(1)).save(any(Booking.class));

        // Assertions
        assertEquals("redirect:/bookings", result);
    }



    @Test
    void delete()  {
        ArgumentCaptor<Long> idCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(bookingRepository).deleteById(idCapture.capture());
        bookingRepository.deleteById(1L);
        assertEquals(1L,idCapture.getValue());
        verify(bookingRepository, times(1)).deleteById(1L);

    }
}