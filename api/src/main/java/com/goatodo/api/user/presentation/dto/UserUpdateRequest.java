package com.goatodo.api.user.presentation.dto;

import com.goatodo.domain.user.Occupation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserUpdateRequest(

        @NotBlank(message = "회원의 닉네임은 null 이거나 공백 일 수 없습니다.")
        @Length(min = 2, max = 15, message = "닉네임은 2글자 이상 15자 이내여야 합니다.")
        String nickname,

        @NotNull(message = "회원의 직업은 null 일 수 없습니다.")
        Occupation occupation
) {

}
