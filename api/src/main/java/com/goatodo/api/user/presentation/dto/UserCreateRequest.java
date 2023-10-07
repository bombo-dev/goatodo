package com.goatodo.api.user.presentation.dto;

import com.goatodo.common.util.RegexPattern;
import com.goatodo.domain.user.OccupationType;
import com.goatodo.domain.user.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserCreateRequest(

        @Valid
        UserAccountRequest account,

        @Pattern(regexp = RegexPattern.PASSWORD_REGEX, message = "영문자+숫자, 8자 이상 20자 이내이여야 합니다.")
        String password,

        @NotBlank(message = "회원의 닉네임은 null 이거나 공백 일 수 없습니다.")
        @Length(min = 2, max = 15, message = "닉네임은 2글자 이상 15자 이내여야 합니다.")
        String nickname,

        @NotNull(message = "회원의 직업은 null 일 수 없습니다.")
        OccupationType occupationType,

        Role role
) {
}
