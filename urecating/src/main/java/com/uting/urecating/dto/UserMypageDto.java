package com.uting.urecating.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserMypageDto {
    private String login;
    private String userName;
    private String image;
    private String phone;
    private String team;
    private String gender;
}