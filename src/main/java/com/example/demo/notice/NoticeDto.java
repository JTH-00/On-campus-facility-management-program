package com.example.demo.notice;

import com.example.demo.user.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDto {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private UserDTO author;
    private LocalDateTime modifyDate;
}