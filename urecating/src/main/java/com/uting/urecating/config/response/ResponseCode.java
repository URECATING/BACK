package com.uting.urecating.config.response;


import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS_REQUEST(200,201,"데이터 처리 성공", "정상처리"),
    SUCCESS_SEARCH(200, 202, "데이터 조회 성공", "정상처리"),
    SUCCESS_INSERT(200 ,203, "데이터 입력 성공", "정상처리"),
    SUCCESS_UPDATE(200 ,204, "데이터 수정 성공", "정상처리"),
    SUCCESS_LIKE(200 ,205, "좋아요 성공", "정상처리"),
    SUCCESS_JOIN(200 ,206, "참여 성공", "정상처리");


    private final int status;
    private final int code;
    private final String description;
    private final String message;

    ResponseCode(int status, int code, String description, String message) {
        this.status = status;
        this.code = code;
        this.description = description;
        this.message = message;
    }


}
