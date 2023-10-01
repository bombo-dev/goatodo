package com.goatodo.common.todo;

public enum TagType {
    COMMON, INDIVIDUAL;

    public boolean isCommonType() {
        return this == TagType.COMMON;
    }

    public boolean isIndividualType() {
        return this == TagType.INDIVIDUAL;
    }
}
