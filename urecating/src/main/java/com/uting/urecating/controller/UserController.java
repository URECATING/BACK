package com.uting.urecating.controller;

import com.uting.urecating.config.exception.ApiException;
import com.uting.urecating.config.exception.ErrorCode;
import com.uting.urecating.config.response.ApiResponse;
import com.uting.urecating.config.response.ResponseCode;
import com.uting.urecating.domain.SiteUser;
import com.uting.urecating.dto.UserJoinDto;
import com.uting.urecating.dto.UserLoginDto;
import com.uting.urecating.dto.UserMypageDto;
import com.uting.urecating.dto.UserUpdateDto;
import com.uting.urecating.jwt.TokenProvider;
import com.uting.urecating.s3.S3Service;
import com.uting.urecating.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final S3Service s3Service;

    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<SiteUser>> join(@Valid @RequestBody UserJoinDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ApiException(ErrorCode.JOIN_DATA_ERROR);
        }

        try {
            SiteUser newUser = userService.join(user.getUserName(), user.getLogin(),
                    user.getPassword(), user.getTeam(), user.getGender(), user.getPhone());
            ApiResponse<SiteUser> response = new ApiResponse(ResponseCode.SUCCESS_JOIN, newUser);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.JOIN_DUPLI_ERROR);        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<SiteUser>> login(@RequestBody UserLoginDto userLoginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getLogin(), userLoginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //JWT생성
            String token = tokenProvider.createToken(authentication);
            SiteUser user = userService.login(userLoginDto.getLogin(), userLoginDto.getPassword());
            ApiResponse<SiteUser> response = new ApiResponse(ResponseCode.SUCCESS_LOGIN, user, token);
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
            ApiResponse<SiteUser> response = new ApiResponse(ResponseCode.SUCCESS_SEARCH_MYPAGE, user);
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.SEARCH_ERROR);
         }
    }

  // 마이페이지 UPDATE
    @PatchMapping(value = "/mypage", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateMyPage(
            @RequestHeader(value = "Authorization", required = false) String tokenInfo,
            @ModelAttribute UserUpdateDto user) throws IOException {

        // 토큰에서 로그인 ID 추출
        String userLogin = tokenProvider.getUserLoginToken(tokenInfo);

        // 이미지 파일이 있을 경우 S3에 업로드
        String imageUrl = null;
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            String fileName = UUID.randomUUID().toString() + user.getImage().getOriginalFilename();
            imageUrl = s3Service.upload(user.getImage(), fileName);
        }else{
            imageUrl = null;
        }

        // 유저 정보 업데이트
        SiteUser updatedUser = userService.updateUser(userLogin, user.getPassword(), imageUrl);

        // 성공 메시지 및 업데이트된 유저 정보 반환
        ApiResponse<SiteUser> response = new ApiResponse(ResponseCode.SUCCESS_UPDATE_MYPAGE, updatedUser);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    //현재 사용자 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<UserMypageDto>> getMyPage(
            @RequestHeader(value = "Authorization", required = false) String tokenInfo) {

        try {
            // JWT 토큰에서 로그인 ID 추출
            String userLogin = tokenProvider.getUserLoginToken(tokenInfo);

            // 로그인 ID로 사용자 정보 조회
            SiteUser user = userService.findByLogin(userLogin);

            // DTO로 변환
            UserMypageDto userMypageDto = new UserMypageDto();
            userMypageDto.setLogin(user.getLogin());
            userMypageDto.setUserName(user.getUserName());
            userMypageDto.setImage(user.getImage());
            userMypageDto.setPhone(user.getPhone());
            userMypageDto.setTeam(user.getTeam());
            userMypageDto.setGender(user.getGender());

            // 성공 메시지 및 사용자 정보 반환
            ApiResponse<UserMypageDto> response = new ApiResponse<>(ResponseCode.SUCCESS_SEARCH_MYPAGE, userMypageDto);
            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (IllegalArgumentException e) {
            throw new ApiException(ErrorCode.SEARCH_ERROR);
        }
    }
}
