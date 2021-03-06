/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 *
 * @author leiyang
 */
public class DateUtils {

    public static Date getDateForNextNthYear(Integer n) {
        Calendar calendar = getCalendarForNextNthYear(n);
        return calendar.getTime();
    }

    public static Date getLastNthBeginOfMonth(Integer n) {
        Calendar calendar = getCalendarForLastNthMonth(n);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getLastNthEndOfMonth(Integer n) {
        Calendar calendar = getCalendarForLastNthMonth(n);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getLastBeginOfMonth() {
        Calendar calendar = getCalendarForLastMonth();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getLastEndOfMonth() {
        Calendar calendar = getCalendarForLastMonth();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        return calendar.getTime();
    }

    public static Date getBeginOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getMidOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getEndOfMonth() {
        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar);
        return calendar.getTime();
    }

    public static Date getBeginOfDay() {
        Calendar calendar = getCalendarForNow();
        setTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    public static Date getEndOfDay() {
        Calendar calendar = getCalendarForNow();
        setTimeToEndofDay(calendar);
        return calendar.getTime();
    }

    public static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public static Calendar getCalendarForLastMonth() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }

    public static Calendar getCalendarForLastNthMonth(Integer n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -n);
        return calendar;
    }

    public static Calendar getCalendarForNextNthYear(Integer n) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, n);
        return calendar;
    }

    public static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public static Date setTimeToBeginningOfDay(Date day) {
        Calendar calendar = dateToCalender(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    public static Date setTimeToEndofDay(Date day) {
        Calendar calendar = dateToCalender(day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static int yearDifference(Date date1, Date date2) {
        Calendar startCalendar = dateToCalender(date1);
        Calendar endCalendar = dateToCalender(date2);

        return endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    }

    public static int yearDifference(Calendar startCalendar, Calendar endCalendar) {
        return endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
    }

    public static int monthDifference(Date date1, Date date2) {
        Calendar startCalendar = dateToCalender(date1);
        Calendar endCalendar = dateToCalender(date2);
        Integer diffYear = yearDifference(startCalendar, endCalendar);
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    public static int monthDifference(Calendar startCalendar, Calendar endCalendar) {
        Integer diffYear = yearDifference(startCalendar, endCalendar);
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    public static int dayDifference(Date date1, Date date2) {
        Calendar dayOne = dateToCalender(date1);
        Calendar dayTwo = dateToCalender(date2);

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public static int calculateAge(Date birthday) {
        int ageDiff = yearDifference(birthday, new Date());
        Calendar cal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        cal.setTime(birthday);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        if (month < now.get(Calendar.MONTH)) {
            return ageDiff;
        } else if (month == now.get(Calendar.MONTH)) {
            if (day <= now.get(Calendar.DATE)) {
                return ageDiff;
            } else {
                return ageDiff - 1;
            }
        }
        return ageDiff - 1;

    }

    //date2-date1
    public static int dayDifferenceWithSign(Date date1, Date date2) {
        int dayDifference = dayDifference(date1, date2);
        if (date1.before(date2)) {
            return dayDifference;
        } else {
            return (0 - dayDifference);
        }
    }

    public static int dayDifference(Calendar dayOne, Calendar dayTwo) {

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }
            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    public static Calendar dateToCalender(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Integer getDayNumber(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static Integer getMonthNumber(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static Integer getYearNumber(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static Calendar addDaysToDate(Calendar cal, Integer days) {
        cal.add(Calendar.DATE, days);
        return cal;
    }

    public static Date addYearsToDate(Date date, Integer years) {
        Calendar cal = dateToCalender(date);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    public static String readableDate(Date date) {
        SimpleDateFormat dt1 = new SimpleDateFormat("dd MMM, h:mm a");
        return dt1.format(date);
    }

    public static String normalDisplayDate(Date date) {
        SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
        return dt1.format(date);
    }

    public static EnumUtils.Month getStringMonth(Integer month) {
        if (month == 0) {
            return EnumUtils.Month.JANUARY;
        } else if (month == 1) {
            return EnumUtils.Month.FEBRUARY;
        } else if (month == 2) {
            return EnumUtils.Month.MARCH;
        } else if (month == 3) {
            return EnumUtils.Month.APRIL;
        } else if (month == 4) {
            return EnumUtils.Month.MAY;
        } else if (month == 5) {
            return EnumUtils.Month.JUNE;
        } else if (month == 6) {
            return EnumUtils.Month.JULY;
        } else if (month == 7) {
            return EnumUtils.Month.AUGUST;
        } else if (month == 8) {
            return EnumUtils.Month.SEPTEMBER;
        } else if (month == 9) {
            return EnumUtils.Month.OCTOBER;
        } else if (month == 10) {
            return EnumUtils.Month.NOVEMBER;
        } else if (month == 11) {
            return EnumUtils.Month.DECEMBER;
        } else {
            return null;
        }
    }

    public static Date randomDate() {
        Random random = new Random();
        int minDay = (int) LocalDate.of(2015, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2016, 11, 07).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);

        LocalDate randomLocalDate = LocalDate.ofEpochDay(randomDay);

        Date date = Date.from(randomLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

}
