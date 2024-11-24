package com.example.demo.place.entity;

import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name="place")
public class Place {
    @Id
    private String placeid = UUID.randomUUID().toString();

    private String placename;

    private String placelocation;

    @ManyToOne
    @JoinColumn(name="userhash")
    User user;
}
