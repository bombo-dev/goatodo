package com.bombo.goatodo.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // BAD_REQUEST
    NOT_EXIST_ID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 ID가 전달되었습니다."),

    MEMBER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 중복되었습니다."),
    MEMBER_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임이 중복되었습니다."),
    MEMBER_DUPLICATE_PASSWORD(HttpStatus.BAD_REQUEST, "동일한 패스워드로 변경은 불가능합니다."),
    MEMBER_NOT_PRESENT(HttpStatus.BAD_REQUEST, "회원이 존재하지 않습니다.");

    private final HttpStatus errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
