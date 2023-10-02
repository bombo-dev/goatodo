package com.goatodo.domain.member.response;

import java.util.Collections;
import java.util.List;

public record MembersResponse(
        List<MemberResponse> memberResponseList
) {

    public List<MemberResponse> getMemberResponseList() {
        return Collections.unmodifiableList(memberResponseList);
    }
}
