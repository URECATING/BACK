package com.uting.urecating.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserJoinDto {
    private String userName;
    private String login;
    private String password;
    private String team;
    private String gender;
    private String phone;
}
