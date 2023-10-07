package com.goatodo.domain.user;

public enum OccupationType {
    EMPLOYEE("직장인"),
    GRADUATE_STUDENT("대학원생"),
    UNIVERSITY_STUDENT("대학생"),
    HIGH_SCHOOL_STUDENT("고등학생"),
    MIDDLE_SCHOOL_STUDENT("중학생"),
    ELEMENTARY_SCHOOL_STUDENT("초등학생"),
    GENERAL("일반인");

    private final String description;

    OccupationType(String description) {
        this.description = description;
    }
}
