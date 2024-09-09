package com.uting.urecating.config.response;

import lombok.Getter;

@Getter
public class ApiResponse <T>{
    private int status;
    private String code;
    private String message;
    private String description;
    private T data;


    public ApiResponse(ResponseCode responseCode, T data) {
        this.status = responseCode.getStatus();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.description = responseCode.getDescription();
        this.data = data;
    }

    public ApiResponse(ResponseCode responseCode) {
        this(responseCode, null);  // 데이터를 null로 처리
    }
}
