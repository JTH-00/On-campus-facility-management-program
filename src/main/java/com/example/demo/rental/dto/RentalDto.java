package com.example.demo.rental.dto;

import com.example.demo.place.dto.PlaceDto;
import com.example.demo.user.dto.UserDTO;
import lombok.Data;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Data
public class RentalDto {
    private String rentalid= UUID.randomUUID().toString();
    private LocalDate rentaldate;
    private LocalTime rentaltime;
    private UserDTO user;
    private PlaceDto place;
}
