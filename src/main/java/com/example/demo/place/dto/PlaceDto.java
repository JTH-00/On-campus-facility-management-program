package com.example.demo.place.dto;

import com.example.demo.user.dto.UserDTO;
import lombok.Data;

@Data
public class PlaceDto {
    private String placeid;
    private String placename;
    private String placelocation;
    private UserDTO user;
}
