package com.core.utility;

import com.core.constant.DateConstant;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Set;

public final class DateUtility {

    private DateUtility() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Set<String> getAllTimeZones() {
        return ZoneId.getAvailableZoneIds();
    }

    public static ZonedDateTime getZonedDateTime() {
        return getZonedDateTimeWithTimeZone(DateConstant.UTC);
    }

    public static ZonedDateTime getZonedDateTimeWithTimeZone(String timeZone) {
        return ZonedDateTime.now(ZoneId.of(timeZone));
    }

    public static LocalDate getLocalDate() {
        return getZonedDateTime().toLocalDate();
    }

    public static LocalDate getLocalDateWithTimeZone(String timeZone) {
        return getZonedDateTimeWithTimeZone(timeZone).toLocalDate();
    }

    public static ZonedDateTime convertToZonedDateTimeFromLocalDate(LocalDate localDate, String timezone) {
        return localDate.atStartOfDay().atZone(ZoneId.of(timezone));
    }

    public static ZonedDateTime convertToZonedDateTimeFromLocalDateEndOfDay(LocalDate localDate, String timezone) {
        return localDate.atTime(23, 59, 59).atZone(ZoneId.of(timezone));
    }

    public static LocalTime getLocalTime() {
        return getZonedDateTime().toLocalTime();
    }

    public static LocalTime getLocalTimeWithTimeZone(String timeZone) {
        return getZonedDateTimeWithTimeZone(timeZone).toLocalTime();
    }

    public static ZonedDateTime getDayBeforeOrAfterCurrentDay(int numberOfDays) {
        return getZonedDateTime().plusDays(numberOfDays).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getDayBeforeOrAfterDay(ZonedDateTime zonedDateTime, int numberOfDays) {
        return zonedDateTime.plusDays(numberOfDays).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime addMonthsInDate(ZonedDateTime zonedDateTime, int numberOfMonths) {
        return zonedDateTime.plusMonths(numberOfMonths);
    }

    public static ZonedDateTime addYearsInDate(ZonedDateTime zonedDateTime, int numberOfYears) {
        return zonedDateTime.plusYears(numberOfYears);
    }

    public static long getTimeDifferenceInDays(ZonedDateTime date1, ZonedDateTime date2) {
        return Duration.between(date2, date1).toDays();
    }

    public static long getTimeDifferenceInHours(ZonedDateTime date1, ZonedDateTime date2) {
        return Duration.between(date2, date1).toHours();
    }

    public static long getTimeDifferenceInMinutes(ZonedDateTime date1, ZonedDateTime date2) {
        return Duration.between(date2, date1).toMinutes();
    }

    public static long getTimeDifferenceInMonths(LocalDate date1, LocalDate date2) {
        Period period = Period.between(date2, date1);
        return period.toTotalMonths();
    }

    public static long getTimeDifferenceInYears(LocalDate date1, LocalDate date2) {
        return Period.between(date2, date1).getYears();
    }

    public static long getTimeDifference(LocalTime time1, LocalTime time2) {
        return Duration.between(time2, time1).getSeconds();
    }

    public static long getDifferenceBetweenDaysIncludingBothStartDateAndEndDate(ZonedDateTime date1, ZonedDateTime date2) {
        long diffInDays = Duration.between(date2.truncatedTo(ChronoUnit.DAYS), date1.truncatedTo(ChronoUnit.DAYS)).toDays();
        return diffInDays >= 0 ? diffInDays + 2 : diffInDays - 2;
    }

    public static long getDifferenceBetweenDaysIncludingEndDate(ZonedDateTime date1, ZonedDateTime date2) {
        long diffInDays = Duration.between(date2.truncatedTo(ChronoUnit.DAYS), date1.truncatedTo(ChronoUnit.DAYS)).toDays();
        return diffInDays >= 0 ? diffInDays + 1 : diffInDays - 1;
    }

    public static ZonedDateTime addSecondsToDate(ZonedDateTime zonedDateTime, int numberOfSeconds) {
        return zonedDateTime.plusSeconds(numberOfSeconds);
    }

    public static ZonedDateTime addMinutesToDate(ZonedDateTime zonedDateTime, int numberOfMinutes) {
        return zonedDateTime.plusMinutes(numberOfMinutes);
    }

    public static ZonedDateTime addHoursToDate(ZonedDateTime zonedDateTime, int numberOfHours) {
        return zonedDateTime.plusHours(numberOfHours);
    }

    public static String formatZonedDateTime(ZonedDateTime zonedDateTime, String format) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatLocalDate(LocalDate localDate, String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatLocalTime(LocalTime localTime, String format) {
        return localTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static ZonedDateTime toZonedDateTime(LocalDate localDate, LocalTime localTime, String zoneId) {
        return ZonedDateTime.of(localDate, localTime, ZoneId.of(zoneId));
    }

    public static ZonedDateTime toZonedDateTimeWithUTC(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of(DateConstant.UTC));
    }

    public static String formatDateWithTimeZone(ZonedDateTime zonedDateTime, String format) {
        return formatDateWithTimeZone(zonedDateTime, format, DateConstant.UTC);
    }

    public static String formatDateWithTimeZone(ZonedDateTime zonedDateTime, String format, String zoneId) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of(zoneId)).format(DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate parseToLocalDate(String value, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(value, formatter);
    }

    public static LocalTime parseToLocalTime(String value, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalTime.parse(value, formatter);
    }

    private static DayOfWeek getDayOfWeek() {
        return WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
    }

    public static LocalDate getFirstDayOfTheWeek() {
        return getLocalDate().with(TemporalAdjusters.previousOrSame(getDayOfWeek()));
    }

    public static LocalDate getLastDayOfTheWeek() {
        DayOfWeek lastDayOfWeek = DayOfWeek.of(((getDayOfWeek().getValue() + 5) % DayOfWeek.values().length) + 1);
        return getLocalDate().with(TemporalAdjusters.nextOrSame(lastDayOfWeek));
    }

    public static ZonedDateTime getStartDateByMonthYear(int month, int year) {
        return getZonedDateTime().withMonth(month).withYear(year).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getEndDateByMonthYear(int month, int year) {
        ZonedDateTime date = getZonedDateTime().withMonth(month).withYear(year);
        YearMonth ym = YearMonth.of(year, month);
        return date.withDayOfMonth(ym.lengthOfMonth())
                .withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    public static ZonedDateTime getStartOfToday() {
        return getZonedDateTime().truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getStartOfDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.truncatedTo(ChronoUnit.DAYS);
    }

    public static ZonedDateTime getEndOfToday() {
        ZonedDateTime now = getZonedDateTime();
        return now.withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    public static ZonedDateTime getEndOfDate(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    public static int getDaysInMonth(ZonedDateTime zonedDateTime) {
        YearMonth yearMonth = YearMonth.of(zonedDateTime.getYear(), zonedDateTime.getMonth());
        return yearMonth.lengthOfMonth();
    }

    public static int getWeekNumberInMonth(ZonedDateTime zonedDateTime) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return zonedDateTime.get(weekFields.weekOfMonth());
    }
}

