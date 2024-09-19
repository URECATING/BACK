package com.uting.urecating.config.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	JOIN_DATA_ERROR(500, "회원가입  - 정확한 값을 입력하세요.", 415),
	JOIN_DUPLI_ERROR(400, "회원가입 - 이미 존재하는 사용자입니다.", 416),
	SEARCH_ERROR(400, "데이터 조회 실패", 417),
	COMMENT_INSERT_ERROR(400, "댓글 작성 실패, 게시글이 존재 하지 않습니다. ", 418),
	COMMENTREPLY_INSERT_ERROR(400, "댓글 작성 실패, 부모 댓글을 찾을 수 없습니다.", 418),
	COMMENT_SEARCH_ERROR(400, "댓글 조회 실패, 게시글이 존재 하지 않습니다.", 419),
	COMMENT_UPDATE_ID_ERROR(400, "댓글 수정 실패, 댓글이 존재 하지 않습니다.", 420),
	COMMENT_DELETE_ERROR(400, "댓글 삭제 실패, 댓글이 존재 하지 않습니다.", 421),
	POST_JOIN_ERROR(400, "댓글 삭제 실패, 댓글이 존재 하지 않습니다.", 422),

	POST_SEARCH_ERROR(400, "게시글 조회 실패, 게시글이 존재하지 않습니다", 423),
	POST_CREATE_ERROR(400, "게시글 작성 실패, 사용자가 존재하지 않습니다", 424),
	POST_UPDATE_USER_ERROR(400, "게시글 수정 실패, 사용자가 존재하지 않습니다", 425),
	POST_UPDATE_POST_ERROR(400, "게시글 수정 실패, 게시글이 존재하지 않습니다", 426),
	POST_DELETE_ERROR(400, "게시글 삭제 실패, 게시글이 존재하지 않습니다", 427),
	POST_CATEGORY_ERROR(400, "카테고리별 게시글 조회 실패, 사용자가 존재하지 않습니다", 428),
	POST_USER_USER_ERROR(400, "사용자 작성 게시글 조회 실패, 사용자가 존재하지 않습니다", 429),
	POST_USER_POST_ERROR(400, "사용자 작성 게시글 조회 실패, 게시글이 존재하지 않습니다", 430);


	private final int status;
	private final String message;
	private final int code;

	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
		this.code = status;
	}
}
