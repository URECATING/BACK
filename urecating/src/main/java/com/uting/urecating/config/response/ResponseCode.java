package com.uting.urecating.config.response;


import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS_JOIN(200, 201, "회원 가입 성공", "정상처리"),
    SUCCESS_LOGIN(200, 202, "로그인 성공", "정상처리"),
    SUCCESS_SEARCH_MYPAGE(200, 203, "마이페이지 조회 성공", "정상처리"),
    SUCCESS_UPDATE_MYPAGE(200, 204, "마이페이지 수정 성공", "정상처리"),
    SUCCESS_LIKE(200, 205, "좋아요 성공", "정상처리"),
    SUCCESS_SEARCH_COMMENT(200, 206, "댓글 조회 성공", "정상처리"),
    SUCCESS_INSERT_COMMENT(200, 207, "댓글 입력 성공", "정상처리"),
    SUCCESS_UPDATE_COMMENT(200, 208, "댓글 수정 성공", "정상처리"),
    SUCCESS_DELETE_COMMENT(200, 209, "댓글 삭제 성공", "정상처리"),
    SUCCESS_JOIN_POST(200, 210, "참가 성공", "정상처리"),
    SUCCESS_SEARCH_JOIN_POST(200, 211, "참가 목록 검색 성공", "정상처리"),
    SUCCESS_DELETE_JOIN_POST(200, 211, "참가 삭제 성공", "정상처리"),
    SUCCESS_LIKE_POST(200, 211, "좋아요 성공", "정상처리"),
    SUCCESS_DELETE_LIKE_POST(200, 211, "좋아요 삭제 성공", "정상처리"),
    SUCCESS_CHECK_LIKE_POST(200, 211, "좋아요 확인 성공", "정상처리"),
    SUCCESS_SEARCH_LIKE_POST(200, 211, "좋아요 목록 검색 성공", "정상처리"),
    SUCCESS_SEARCH_POST(200, 211, "게시글 조회 성공", "정상처리"),
    SUCCESS_CREATE_POST(200, 212, "게시글 작성 성공", "정상처리"),
    SUCCESS_UPDATE_POST(200, 213, "게시글 수정 성공", "정상처리"),
    SUCCESS_DELETE_POST(200, 214, "게시글 삭제 성공", "정상처리"),
    SUCCESS_CATEGORY_POST(200, 215, "카테고리별 게시글 조회 성공", "정상처리"),
    SUCCESS_USER_POST(200, 216, "사용자 작성 게시글 조회 성공", "정상처리");


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
