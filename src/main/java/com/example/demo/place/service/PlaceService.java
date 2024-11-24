package com.example.demo.place.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.place.dto.PlaceDto;
import com.example.demo.place.entity.Place;
import com.example.demo.place.repository.PlaceRepository;
import com.example.demo.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceService {
    @Autowired
    PlaceRepository placeRepository;

    private final ModelMapper modelMapper;


    private PlaceDto of(Place place) {
        return this.modelMapper.map(place, PlaceDto.class);
    }

    public PlaceDto getPlaceDto(String placeid) {
        Optional<Place> place = this.placeRepository.findById(placeid);
        if (place.isPresent()) {
            return of(place.get());
        } else {
            throw new DataNotFoundException("place not found");
        }
    }

    public Place getPlace(String placeid) {
        Optional<Place> place = this.placeRepository.findById(placeid);
        if (place.isPresent()) {
            return place.get();
        } else {
            throw new DataNotFoundException("place not found");
        }
    }

    public void addPlace(String PlaceName, String PlaceLocation, User user){
        Place place=new Place();
        place.setPlacename(PlaceName);
        place.setPlacelocation(PlaceLocation);
        place.setUser(user);
        this.placeRepository.save(place);
    }

    public void delete(PlaceDto placeDto){
        placeRepository.deleteById(placeDto.getPlaceid());
    }

    public List<Place> findPlaces() {
        return placeRepository.findAll();
    }

}

