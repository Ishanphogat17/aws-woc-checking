package com.core.utility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MethodLoggerUtility {
    private MethodLoggerUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void start(Object caller) {
        String className = caller.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("[ [ {} | {}() ] started ]", className, methodName);
    }

    public static void end(Object caller) {
        String className = caller.getClass().getSimpleName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("[ [ {} | {}() ] ended ]", className, methodName);
    }
}
