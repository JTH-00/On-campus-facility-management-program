package com.example.demo.rental.service;

import com.example.demo.place.entity.Place;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.repository.RentalRepository;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RentalService {
    private  final RentalRepository rentalRepository;

    public void createRental(Place place, User user, LocalDate rentaldate, LocalTime rentaltime){
        Rental rental=new Rental();
        rental.setPlace(place);
        rental.setUser(user);
        rental.setRentaldate(rentaldate);
        rental.setRentaltime(rentaltime);
        this.rentalRepository.save(rental);
    }

    public Optional<Rental> getRental(String rentalid){
        return rentalRepository.findById(rentalid);
    }


    public List<Rental> findRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> findRentalsByUser(User user) {
        return rentalRepository.findByUser(user);
    }

    public List<Rental> findRentalsByPlace(Place place){return rentalRepository.findByPlace(place);}

    public void delete(Rental rental){
        rentalRepository.deleteById(rental.getRentalid());
    }
    public long getRentalCount() {
        return rentalRepository.count();
    }

    public boolean isReserved(LocalDate rentaldate, LocalTime rentaltime, Place place){
        List<Rental> rentals = rentalRepository.findByRentaldateAndRentaltimeAndPlace(rentaldate,rentaltime,place);
        if(rentals.isEmpty()){
            return false;
        }else{
            return true;
        }
    }


}
