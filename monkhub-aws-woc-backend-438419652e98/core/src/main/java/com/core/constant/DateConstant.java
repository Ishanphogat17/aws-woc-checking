package com.core.constant;

public final class DateConstant {

    private DateConstant() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static final String ZONED_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX'['VV']'";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String TIME_PATTERN = "HH:mm:ss";

    public static final String X_TIMEZONE = "X-TimeZone";

    public static final String TIME_AM_PM_PATTERN = "HH:mm a";

    public static final String UTC = "UTC";
}