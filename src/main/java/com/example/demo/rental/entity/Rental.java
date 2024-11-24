package com.example.demo.rental.entity;

import com.example.demo.place.entity.Place;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"placeid", "rentaldate", "rentaltime"}),name="rental")
public class Rental {
    @Id
    private String rentalid= UUID.randomUUID().toString();

    private LocalDate rentaldate;

    private LocalTime rentaltime;

    @ManyToOne
    @JoinColumn(name="userhash")
    private User user;

    @ManyToOne
    @JoinColumn(name="placeid")
    private Place place;
}
