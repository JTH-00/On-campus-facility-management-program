package com.example.demo.rental.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.core.MethodParameter;

import java.sql.Time;
import java.time.*;

@Data
public class RentalCreateForm {
    @NotNull(message = "시설 이용 시간은 필수항목입니다.")
    private LocalTime rentaltime;
    @NotNull(message = "시설 이용 날짜는 필수항목입니다.")
    private LocalDate rentaldate;


}

