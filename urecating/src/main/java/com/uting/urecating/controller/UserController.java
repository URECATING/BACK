package com.uting.urecating.controller;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserJoinDto;
import com.uting.urecating.dto.UserLoginDto;
import com.uting.urecating.dto.UserUpdateDto;
import com.uting.urecating.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //회원가입
    @PostMapping("/join")
    public SiteUser join(@RequestBody UserJoinDto user) {
        return userService.join(user.getUserName(), user.getLogin(),
                user.getPassword(), user.getTeam(), user.getGender(), user.getPhone());
    }

    //로그인
    @PostMapping("/login")
    public SiteUser login(@RequestBody UserLoginDto userLoginDto){
        return userService.login(userLoginDto.getLogin(), userLoginDto.getPassword());
    }

    //마이페이지 조회
    @GetMapping("/{id}/mypage")
    public SiteUser getMyPage(@PathVariable("id") Long id) {
        System.out.println(userService.getUserById(id).getUserName());
        return userService.getUserById(id);
    }

    //마이페이지 UPDATE
    @PatchMapping("/{id}/mypage")
    public SiteUser updateMyPage(@PathVariable("id") Long id, @RequestBody UserUpdateDto updateDto) {
        return userService.updateUser(id, updateDto);
    }
}
