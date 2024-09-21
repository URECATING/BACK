package com.uting.urecating.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserUpdateDto {
    private String password;
    private MultipartFile image;  // 파일 업로드를 위한 필드
}