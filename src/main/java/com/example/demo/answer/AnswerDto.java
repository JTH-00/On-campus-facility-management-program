package com.example.demo.answer;

import com.example.demo.question.QuestionDto;
import com.example.demo.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class AnswerDto {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private QuestionDto question;
    private UserDTO author;
    private LocalDateTime modifyDate;
    private Set<UserDTO> voter;
}