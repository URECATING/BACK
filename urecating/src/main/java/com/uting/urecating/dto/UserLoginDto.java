package com.uting.urecating.dto;

import com.uting.urecating.domain.Comment;
import com.uting.urecating.domain.Post;
import com.uting.urecating.domain.SiteUser;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserLoginDto {
    private String login;
    private String password;

}