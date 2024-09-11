package com.uting.urecating.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserUpdateDto {
    private String image;
    private String password;
    private String phone;
}
