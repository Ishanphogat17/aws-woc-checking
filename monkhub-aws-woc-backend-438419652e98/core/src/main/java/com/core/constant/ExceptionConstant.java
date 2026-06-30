package com.core.constant;

public final class ExceptionConstant {
    private ExceptionConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String EXCEPTION_OCCURRED = "[ {} | {}() ] Exception Occurred message is {}";
    public static final String EXCEPTION_OCCURRED_CAUSE = "[ {} | {}() ] Exception Occurred message is {}, cause is {}";
    public static final String USER_NOT_FOUND = "User not found";
}
