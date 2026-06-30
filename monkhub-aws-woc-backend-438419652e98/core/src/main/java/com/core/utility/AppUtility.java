package com.core.utility;

import com.core.constant.AppConstant;
import com.core.enums.FilterType;
import com.core.enums.SortBy;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;

import java.time.ZonedDateTime;

public final class AppUtility {
    private AppUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Sort getSortBy(SortBy sortType, String fieldName) {
        if (sortType == null) {
            return Sort.unsorted();
        }

        return switch (sortType) {
            case LATEST -> Sort.by(Sort.Direction.DESC, AppConstant.CREATED_AT);
            case OLDEST -> Sort.by(Sort.Direction.ASC, AppConstant.CREATED_AT);
            case ALPHABETICAL_ASCENDING ->
                    Sort.by(Sort.Order.by(fieldName).ignoreCase().with(Sort.Direction.ASC));
            case ALPHABETICAL_DESCENDING ->
                    Sort.by(Sort.Order.by(fieldName).ignoreCase().with(Sort.Direction.DESC));
        };
    }

    public static Pair<ZonedDateTime, ZonedDateTime> getDateRange(FilterType filterType) {
        ZonedDateTime endDateTime = DateUtility.getZonedDateTime();
        switch (filterType) {
            case TODAY -> {
                ZonedDateTime startDateTime = DateUtility.getStartOfDate(endDateTime);
                return Pair.of(startDateTime, endDateTime);
            }
            case LAST_SEVEN_DAYS -> {
                ZonedDateTime startDateTime = DateUtility.getStartOfDate(DateUtility.getDayBeforeOrAfterDay(endDateTime, -6));
                return Pair.of(startDateTime, endDateTime);
            }
            case LAST_THIRTY_DAYS -> {
                ZonedDateTime startDateTime = DateUtility.getStartOfDate(DateUtility.getDayBeforeOrAfterDay(endDateTime, -29));
                return Pair.of(startDateTime, endDateTime);
            }
            default -> throw new IllegalArgumentException("Unsupported filter type: " + filterType);
        }
    }
}
