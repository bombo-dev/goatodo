package com.goatodo.application.user.dto;

import java.util.Collections;
import java.util.List;

public record UsersResponse(
        List<UserResponse> userResponseList
) {

    public List<UserResponse> getMemberResponseList() {
        return Collections.unmodifiableList(userResponseList);
    }
}
