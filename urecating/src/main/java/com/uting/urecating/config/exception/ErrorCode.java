package com.uting.urecating.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	JOIN_DATA_ERROR(500,"회원가입  - 정확한 값을 입력하세요.",415),
	JOIN_DUPLI_ERROR(400,"회원가입 - 이미 존재하는 사용자입니다.",416),
	SEARCH_ERROR(400,"데이터 조회 실패",417);



	private final int status;
	private final String message;
	private final int code;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
		this.code = status;
	}
}
