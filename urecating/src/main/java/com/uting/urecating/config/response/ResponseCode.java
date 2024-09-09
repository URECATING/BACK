package com.uting.urecating.config.response;


import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS_REQUEST(200,"0000","데이터 처리 성공", "정상처리"),
    SUCCESS_SEARCH(200, "0000", "데이터 조회 성공", "정상처리"),
    SUCCESS_INSERT(200 ,"0000", "데이터 입력 성공", "정상처리"),
    SUCCESS_LIKE(200 ,"0000", "좋아요 성공", "정상처리"),
    SUCCESS_JOIN(200 ,"0000", "참여 성공", "정상처리");


    private final int status;
    private final String code;
    private final String description;
    private final String message;

    ResponseCode(int status, String code, String description, String message) {
        this.status = status;
        this.code = code;
        this.description = description;
        this.message = message;
    }


}
