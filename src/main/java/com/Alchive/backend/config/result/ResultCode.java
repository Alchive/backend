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

    // TOKEN
    TOKEN_ACCESS_SUCCESS("T001", "액세스 토큰 생성 성공"),
    TOKEN_REFRESH_SUCCESS("T002", "리프레쉬 토큰 생성 성공"),

    // PROBLEM
    PROBLEM_UNSUBMIT_SAVE_SUCCESS("P001", "미제출 문제 정보 저장 성공"),
    PROBLEM_SUBMIT_SAVE_SUCCESS("P002", "문제 정보 및 제출한 코드 정보 저장 성공"),
    PROBLEM_DELETE_SUCCESS("U003", "문제 삭제 성공"),
    PROBLEM_DUPLICATED("U004", "문제 정보 중복"),
    PROBLEM_NOT_DUPLICATED("U005", "문제 정보 중복되지 않음"),
    PROBLEM_LIST_SUCCESS("P006", "문제 목록 조회 성공"),
    PROBLEM_PLATFORM_LIST_SUCCESS("P007", "플랫폼 별 문제 조회 성공"),
    PROBLEM_SEARCH_SUCCESS("P008", "문제 검색 결과 조회 성공"),
    PROBLEM_DETAIL_INFO_SUCCESS("P009", "문제 상세정보 조회 성공"),
    PROBLEM_MEMO_UPDATE_SUCCESS("P010", "문제 메모 수정 성공"),

    // SOLUTION
    SOLUTION_CONTENT_UPDATE_SUCCESS("S001", "풀이 내용 수정 성공"),

    ;

    private final String code;
    private final String message;
}
