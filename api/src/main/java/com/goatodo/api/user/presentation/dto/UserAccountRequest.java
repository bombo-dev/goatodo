package com.goatodo.api.user.presentation.dto;

import com.goatodo.common.util.RegexPattern;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public record UserAccountRequest(

        @Pattern(regexp = RegexPattern.EMAIL_REGEX,
                message = "적절한 이메일 양식대로 입력해주세요. ex) goatodo@example.com")
        @Length(max = 50, message = "이메일 ID가 50자를 넘길 수 없습니다.")
        String email,

        @Pattern(regexp = RegexPattern.PASSWORD_REGEX,
                message = "영문자+숫자, 8자 이상 20자 이내이여야 합니다.")
        String password
) {

}
