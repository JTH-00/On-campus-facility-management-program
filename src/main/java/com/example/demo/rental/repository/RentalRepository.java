package com.example.demo.rental.repository;

import com.example.demo.place.entity.Place;
import com.example.demo.rental.entity.Rental;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental,String> {
    List <Rental> findByRentaldateAndRentaltimeAndPlace(LocalDate rentaldate, LocalTime rentaltime, Place place);

    List<Rental> findByUser(User user);
    List<Rental> findByPlace(Place place);
}
