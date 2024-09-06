package com.uting.urecating.controller;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserJoinDto;
import com.uting.urecating.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/join")
    public SiteUser join(UserJoinDto user) {
        return userService.join(user.getUserName(), user.getLogin(),
                user.getPassword(), user.getTeam(), user.getGender(), user.getPhone());
    }

    //로그인

    //마이페이지 READ

    //마이페이지 UPDATE
}
