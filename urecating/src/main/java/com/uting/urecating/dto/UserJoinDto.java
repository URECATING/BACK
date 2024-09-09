package com.uting.urecating.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {
    @NotEmpty(message = "사용자 이름은 필수 항목입니다.")
    private String userName;

    @NotEmpty(message = "아이디는 필수 항목입니다.")
    private String login;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 8자 이상이어야 하며, 영문과 숫자를 포함해야 합니다.")
    private String password;

    @NotEmpty(message = "소속은 필수 항목입니다.")
    private String team;

    @NotEmpty(message = "성별은 필수 항목입니다.")
    private String gender;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    @Pattern(regexp = "^(010[-]?[0-9]{4}[-]?[0-9]{4})$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    private String phone;
}
