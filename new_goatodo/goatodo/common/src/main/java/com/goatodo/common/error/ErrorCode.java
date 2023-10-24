package com.goatodo.common.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // BAD_REQUEST
    INVALID_ARGUMENT(400, "COMMON-001", "잘못된 인자가 전달되었습니다."),
    NOT_EXIST_ID_REQUEST(400, "COMMON-002", "잘못된 ID가 전달되었습니다."),

    MEMBER_LOGIN_FAILED(400, "ACCOUNT-001", "이메일 또는 비밀번호가 틀렸습니다."),
    MEMBER_DUPLICATE_EMAIL(400, "ACCOUNT-002", "이메일이 중복되었습니다."),
    MEMBER_DUPLICATE_NICKNAME(400, "ACCOUNT-003", "닉네임이 중복되었습니다."),
    MEMBER_DUPLICATE_PASSWORD(400, "ACCOUNT-004", "동일한 패스워드로 변경은 불가능합니다."),
    MEMBER_NOT_PRESENT(400, "ACCOUNT-005", "회원이 존재하지 않습니다."),

    TAG_DUPLICATE(400, "TAG-001", "중복된 이름의 태그는 생성 할 수 없습니다."),

    NOT_ACTIVE(400, "TODO-001", "기간이 지난 Todo는 수정이 불가능합니다."),

    // FORBIDDEN
    CREATE_REQUEST_IS_FORBIDDEN(403, "ROLE-001", "생성 할 수 있는 권한이 없습니다."),
    READ_REQUEST_IS_FORBIDDEN(403, "ROLE-002", "조회 할 수 있는 권한이 없습니다."),
    EDIT_REQUEST_IS_FORBIDDEN(403, "ROLE-003", "수정 할 수 있는 권한이 없습니다."),
    DELETE_REQUEST_IS_FORBIDDEN(403, "ROLE-004", "삭제 할 수 있는 권한이 없습니다."),
    FORBIDDEN_EXCEPTION(403, "ROLE-005", "권한이 없습니다."),

    // INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(500, "SERVER-001", "서버에서 오류가 발생했습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
