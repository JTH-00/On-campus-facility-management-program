package com.example.demo.answer;

import com.example.demo.question.Question;
import com.example.demo.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Question question;

    @ManyToOne
    @JoinColumn(name="userhash")
    private User author;

    @ManyToMany
    Set<User> voter;

    private LocalDateTime modifyDate;
}