package com.Alchive.backend.config.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    // USER
    USER_CREATE_SUCCESS("U001", "회원 생성 성공"),
    USER_USERNAME_DUPLICATED("U002", "회원 아이디 중복"),
    USER_USERNAME_NOT_DUPLICATED("U003", "회원 아이디 중복되지 않음"),
    USER_DELETE_SUCCESS("U004", "회원 탈퇴 성공"),
    USER_DETAIL_INFO_SUCCESS("U005", "회원 상세정보 조회 성공"),
    USER_UPDATE_SUCCESS("U008", "회원 상세정보 수정 성공"),

    // AUTH
    TOKEN_ACCESS_SUCCESS("A001", "액세스 토큰 생성 성공"),
    TOKEN_REFRESH_SUCCESS("A002", "리프레쉬 토큰 생성 성공"),

    // SOLUTION
    SOLUTION_CREATE_SUCCESS("S001", "풀이 생성 성공"),
    SOLUTION_UPDATE_SUCCESS("S002", "풀이 수정 성공"),
    SOLUTION_DELETE_SUCCESS("S003", "풀이 삭제 성공"),

    // BOARD
    BOARD_CREATE_SUCCESS("B001", "게시물 생성 성공"),
    BOARD_LIST_INFO_SUCCESS("B002", "게시물 목록 조회 성공"),
    BOARD_INFO_SUCCESS("B003", "게시물 조회 성공"),
    BOARD_NOT_EXIST("B004", "존재하지 않는 게시물"),
    BOARD_MEMO_UPDATE_SUCCESS("B005", "게시물 메모 수정 성공"),
    BOARD_DELETE_SUCCESS("B006", "게시물 삭제 성공"),

    // SNS
    SNS_INFO_SUCCESS("N001", "소셜 조회 성공"),
    SNS_CREATE_SUCCESS("N002", "소셜 생성 성공");
    private final String code;
    private final String message;
}
