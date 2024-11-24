package com.example.demo.user.entity;

import com.example.demo.rank.entity.Rank;
import com.example.demo.role.entity.Role;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "user")
public class User {
    @Id
    private String userhash=UUID.randomUUID().toString();

    @Column(unique = true)
    private String userid;

    private String username;

    private String userpw;

    @Column(unique = true)
    private String userphone;

    @Column(unique = true)
    private String useremail;


    @ManyToOne
    @JoinColumn(name="userrole")
    private Role role;

    @ManyToOne
    @JoinColumn(name="userrank")
    private Rank rank;
}
