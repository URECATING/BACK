package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;

import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserJoinDto;
import com.uting.urecating.dto.UserLoginDto;
import com.uting.urecating.dto.UserUpdateDto;
import com.uting.urecating.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<SiteUser>> join(@Valid @RequestBody UserJoinDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ApiException(ErrorCode.JOIN_DATA_ERROR);
        }
        try {
            SiteUser newUser = userService.join(user.getUserName(), user.getLogin(),
                    user.getPassword(), user.getTeam(), user.getGender(), user.getPhone());

            ApiResponse<SiteUser> response = new ApiResponse<>(ResponseCode.SUCCESS_INSERT, newUser);
            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.JOIN_DUPLI_ERROR);
        }
    }


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SiteUser>>  login(@RequestBody UserLoginDto userLoginDto) {
        try {
            SiteUser user = userService.login(userLoginDto.getLogin(), userLoginDto.getPassword());
            ApiResponse<SiteUser> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH, user);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorCode.SEARCH_ERROR);
        }
    }

    // 마이페이지 조회
    @GetMapping("/{id}/mypage")
    public ResponseEntity<?> getMyPage(@PathVariable("id") Long id) {
        try {
            SiteUser user = userService.getUserById(id);
            ApiResponse<SiteUser> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH, user);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.SEARCH_ERROR); // 사용자 없음
        }
    }

    // 마이페이지 UPDATE
    @PatchMapping("/{id}/mypage")
    public ResponseEntity<?> updateMyPage(@PathVariable("id") Long id, @RequestBody UserUpdateDto updateDto) {
        try {
            SiteUser updatedUser = userService.updateUser(id, updateDto);

            ApiResponse<SiteUser> response = new ApiResponse<>(ResponseCode.SUCCESS_UPDATE, updatedUser);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.SEARCH_ERROR); // 사용자 없음
        }
    }
}
