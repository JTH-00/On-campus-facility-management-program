package com.example.demo.rental.controller;

import com.example.demo.place.entity.Place;
import com.example.demo.place.service.PlaceService;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.entity.RentalCreateForm;
import com.example.demo.rental.service.RentalService;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RentalController {
    private  final RentalService rentalService;
    private final UserService userService;
    private final PlaceService placeService;


    @GetMapping("/reservation/{placeid}")
    @PostMapping("/reservation/{placeid}")
    public String Reservation(RentalCreateForm rentalCreateForm,@PathVariable("placeid") String placeid, Model model){
        Place place=this.placeService.getPlace(placeid);
        model.addAttribute("places",place);
        return "reservation.html";
    }

    @PostMapping("/sendRental/{placeid}")
    public String Reservationing(@Valid RentalCreateForm rentalCreateForm, Principal principal, @PathVariable("placeid") String placeid, Model model) throws Exception{
        User user=this.userService.getUser(principal.getName());
        Place place=this.placeService.getPlace(placeid);
        model.addAttribute("places",place);
        try{
            this.rentalService.createRental(place, user,rentalCreateForm.getRentaldate(),rentalCreateForm.getRentaltime());
        }catch (DataIntegrityViolationException e){
            e.printStackTrace();
            return "redirect:/reservation/{placeid}?error=duplicated";
            // 자바스크립트 alert창으로 에러창표시
            // 시설 예약시간이 중복
        }catch(Exception e) {
            e.printStackTrace();
            return "redirect:/reservation/{placeid}?error=unknown";
            // 나머지 에러
        }
        return "redirect:/user";
    }

    @GetMapping("/admin/user/rental")
    public String UserRental(Model model){
        List<Rental> rentals=rentalService.findRentals();
        model.addAttribute("rentals",rentals);
        return "UserRentalPlace.html";
    }

    @GetMapping("/reservation_info")
    public String RentalInfo(Principal principal,Model model){
        String loginid = principal.getName();
        User user = userService.getUser(loginid);

        LocalDate currentDate = LocalDate.now();
        model.addAttribute("currentDate", currentDate);

        List<Rental> rentals = rentalService.findRentalsByUser(user);
        model.addAttribute("rentals",rentals);
        return "reservationinfo.html";
    }
}
