package com.uting.urecating.config.response;


import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS_JOIN(200, 201, "회원 가입 성공", "정상처리"),
    SUCCESS_LOGIN(200, 202, "로그인 성공", "정상처리"),
    SUCCESS_SEARCH_MYPAGE(200, 203, "마이페이지 조회 성공", "정상처리"),
    SUCCESS_UPDATE_MYPAGE(200, 204, "마이페이지 수정 성공", "정상처리"),
    SUCCESS_LIKE(200, 205, "좋아요 성공", "정상처리");


    private final int status;
    private final int code;
    private final String description;
    private final String message;

    private ResponseCode(int status, int code, String description, String message) {
        this.status = status;
        this.code = code;
        this.description = description;
        this.message = message;
    }
}
