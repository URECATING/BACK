package com.uting.urecating.controller;

import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserJoinDto;
import com.uting.urecating.dto.UserLoginDto;
import com.uting.urecating.dto.UserUpdateDto;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> join(@Valid @RequestBody UserJoinDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();

            bindingResult.getFieldErrors().forEach(fieldError -> {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();

                errorMessages.put(fieldName, errorMessage);
            });
            return ResponseEntity.badRequest().body(Map.of("errors", errorMessages.toString()));
        }

        try {
            SiteUser newUser = userService.join(user.getUserName(), user.getLogin(),
                    user.getPassword(), user.getTeam(), user.getGender(), user.getPhone());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입 성공");
            response.put("user", newUser);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("login", e.getMessage()));
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getLogin(), userLoginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //JWT생성
            String token = tokenProvider.createToken(authentication);

            SiteUser user = userService.login(userLoginDto.getLogin(), userLoginDto.getPassword());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "로그인 성공");
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        }
    }
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDto userLoginDto) {
//        try {
//            SiteUser user = userService.login(userLoginDto.getLogin(), userLoginDto.getPassword());
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "로그인 성공");
//            response.put("user", user);
//            return ResponseEntity.ok(response);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
//        }
//    }

    // 마이페이지 조회
    @GetMapping("/{id}/mypage")
    public ResponseEntity<?> getMyPage(@PathVariable("id") Long id) {
        try {
            SiteUser user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // 사용자 없음
        }
    }

    // 마이페이지 UPDATE
    @PatchMapping("/mypage")
    public ResponseEntity<?> updateMyPage(
            @RequestHeader(value = "Authorization", required = false) String tokenInfo,
            @RequestBody UserUpdateDto updateDto) {

        // 토큰에서 로그인 ID 추출
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);

        // 유저 정보 업데이트
        SiteUser updatedUser = userService.updateUser(userLogin, updateDto);

        // 성공 메시지 및 업데이트된 유저 정보 반환
        Map<String, Object> response = new HashMap<>();
        response.put("message", "마이페이지 정보가 성공적으로 수정되었습니다.");
        response.put("user", updatedUser);

        return ResponseEntity.ok(response);
    }

//    @PatchMapping("/{id}/mypage")
//    public ResponseEntity<?> updateMyPage(@PathVariable("id") Long id, @RequestBody UserUpdateDto updateDto) {
//        try {
//            SiteUser updatedUser = userService.updateUser(id, updateDto);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("message", "정보수정 성공");
//            response.put("user", updatedUser);
//
//            return ResponseEntity.ok(response);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.notFound().build(); // 사용자 없음
//        }
//    }
}
