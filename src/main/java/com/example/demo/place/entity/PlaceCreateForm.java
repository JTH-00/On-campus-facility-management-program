package com.example.demo.place.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlaceCreateForm {
    @NotEmpty(message = "시설 이름은 필수항목입니다.")
    private String placename;

    @NotEmpty(message = "시설 위치는 필수항목입니다.")
    private String placelocation;

}
