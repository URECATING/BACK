package com.uting.urecating.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	POSTS_EMPTY_TITLE(400, "제목을 입력하세요.", 417),
	POSTS_EMPTY_TEST(400, "Test입니다", 418);


	private final int status;
	private final String message;
	private final int code;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
		this.code = status;
	}
}
