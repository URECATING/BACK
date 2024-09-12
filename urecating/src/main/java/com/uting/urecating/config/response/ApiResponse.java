package com.uting.urecating.config.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponse<T> {
        private int status;
        private int code;
        private String message;
        private String description;
        private T data;
        private String token;

        @Builder
        public ApiResponse(ResponseCode responseCode, T data) {
            this.status = responseCode.getStatus();
            this.code = responseCode.getCode();
            this.message = responseCode.getMessage();
            this.description = responseCode.getDescription();
            this.data = data;
        }
        public ApiResponse(ResponseCode responseCode, T data, String token ) {
            this.status = responseCode.getStatus();
            this.code = responseCode.getCode();
            this.message = responseCode.getMessage();
            this.description = responseCode.getDescription();
            this.token = token;
            this.data = data;
        }

        public ApiResponse(int status, String message) {
            this.status = status;
            this.message = message;
            this.description = message;
            this.code = status;
        }


    }
