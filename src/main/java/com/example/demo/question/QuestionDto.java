package com.example.demo.question;

import com.example.demo.answer.AnswerDto;
import com.example.demo.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private List<AnswerDto> answerList;
    private UserDTO author;
    private LocalDateTime modifyDate;
    private Set<UserDTO> voter;
}