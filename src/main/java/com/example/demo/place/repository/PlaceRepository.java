package com.example.demo.place.repository;

import com.example.demo.place.entity.Place;
import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place,String> {
    List<Place> findByUser(User user);
}
