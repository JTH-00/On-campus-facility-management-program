package com.example.demo.user.dto;

import com.example.demo.rank.dto.RankDto;
import com.example.demo.role.dto.RoleDto;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDTO {
    private String userhash;
    private String username;
    private String userid;
    private String userpw;
    private String userphone;
    private String useremail;
    private RoleDto role;
    private RankDto rank;
}
