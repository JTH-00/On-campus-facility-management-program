package com.example.demo.place.controller;

import com.example.demo.place.dto.PlaceDto;
import com.example.demo.place.entity.Place;
import com.example.demo.place.entity.PlaceCreateForm;
import com.example.demo.place.repository.PlaceRepository;
import com.example.demo.place.service.PlaceService;
import com.example.demo.rental.entity.Rental;
import com.example.demo.rental.service.RentalService;
import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PlaceController {

    private final PlaceService placeService;
    private final RentalService rentalService;
    private final UserService userService;

    @GetMapping("/admin/registration")
    public String registration(PlaceCreateForm placeCreateForm){return "registration.html";}

    @PostMapping("/admin/registration")
    public String registrationpost(@Valid PlaceCreateForm placeCreateForm, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            return "registration.html";
        }
        User user=this.userService.getUser(principal.getName());
        this.placeService.addPlace(placeCreateForm.getPlacename(),placeCreateForm.getPlacelocation(),user);
        return "redirect:/adminmainpage";
    }

    @RequestMapping("/admin/facility")
    public String facilitying(Model model){
        List<Place> places=placeService.findPlaces();
        model.addAttribute("places",places);

        return "adminfacility.html";
    }

    @GetMapping("/admin/facility/delete/{placeid}")
    public String deletefacility(Principal principal,@PathVariable("placeid") String placeid){
        PlaceDto placeDto=this.placeService.getPlaceDto(placeid);
        Place place = this.placeService.getPlace(placeid);
        List<Rental> rentals = rentalService.findRentalsByPlace(place);

        if(rentals != null){
            for(int i =0; i < rentals.size(); i++){
                Rental rental = rentals.get(i);
                this.rentalService.delete(rental);
            }
        }
        this.placeService.delete(placeDto);

        return "redirect:/adminmainpage";
    }
}
