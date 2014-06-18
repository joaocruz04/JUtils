package pt.joaocruz.jutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by BEWARE S.A. on 01/11/13.
 */
public class JCal {

    public static enum Type {SECOND, MINUTE, HOUR, DAY, MONTH, YEAR};
    public static enum DifferenceType {SECOND, MINUTE, HOUR, DAY};

    private static int SECONDS = 1000;
    private static int MINUTES = 60;
    private static int HOURS = 60;
    private static int DAYS = 24;

    private static long NDAYS = SECONDS*MINUTES*HOURS*DAYS;
    private static long NHOURS = SECONDS*MINUTES*HOURS;
    private static long NMINUTES = SECONDS*MINUTES;
    private static long NSECONDS = SECONDS;

    public static class DateDifference {
        public long nDays, nHours, nMinutes, nSeconds;
        public boolean firstIsBigger;
        public DateDifference(boolean firstBigger, long days, long hours, long minutes, long seconds) {
            firstIsBigger=firstBigger; nDays = days; nHours = hours; nMinutes = minutes; nSeconds = seconds;
        }
    }

    /**
     * Returns the difference between two Calendars.
     * @param first The first Calendar
     * @param second The second Calendar
     * @param type Difference type (Seconds, Minutes, Hours, Days)
     * @return returns the (first - second) operation
     */

    public static int difference(Calendar first, Calendar second, DifferenceType type) {
        long milis1 = first.getTimeInMillis();
        long milis2 = second.getTimeInMillis();
        long factor;
        switch (type) {
            case SECOND: factor = 1000; break;
            case MINUTE: factor = 1000*60; break;
            case HOUR: factor = 1000*60*60; break;
            case DAY: factor = 1000*60*60*24; break;
            default: factor = 1;
        }
        return (int)(Math.floor(milis1-milis2) / factor);
    }

    /**
     * Returns the difference between two Calendars.
     * @param first The first Calendar
     * @param second The second Calendar
     * @return returns the number of days, hours, minutes and seconds between the two calendars
     */
    public static DateDifference getDifference(Calendar first, Calendar second) {
        long diff = first.getTimeInMillis()-second.getTimeInMillis();
        boolean b = diff>0;
        diff = Math.abs(diff);
        long nDays = diff / NDAYS;
        long remainder =  diff-nDays*NDAYS;
        long nHours = remainder/NHOURS;
        long remainder2 = remainder-nHours*NHOURS;
        long nMinutes = remainder2 / NMINUTES;
        long remainder3 = remainder2 - nMinutes*NMINUTES;
        long nSeconds = remainder3/ NSECONDS;
        return new DateDifference(b, nDays, nHours, nMinutes, nSeconds);
    }


    /**
     * Retuns a copy of the given Calendar
     * @param calendar The original Calendar
     * @return The copy of the original Calendar
     */

    public static Calendar copyOf(Calendar calendar) {
        Calendar c = Calendar.getInstance(java.util.Locale.getDefault());
        if (calendar == null)
            c = null;
        else
            c.setTimeInMillis(calendar.getTimeInMillis());
        return c;
    }


    /**
     * Returns a formatted String from a given Calendar.
     * @param calendar The original Calendar
     * @param pattern The output pattern
     * @return Returns the formatted string with the given pattern
     */

    public static String stringFromCalendar(Calendar calendar, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, java.util.Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    /**
     * Returns a Calendar instance based on String input
     * @param date String with the date
     * @param pattern Pattern that matches the input String
     * @return Returns the Calendar instance based on the String input.
     */

    public static Calendar calendarFromString(String date, String pattern) {
        Calendar calendar = Calendar.getInstance(java.util.Locale.getDefault());
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, java.util.Locale.getDefault());
            calendar.setTime(df.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return calendar;
    }

    /**
     * Returns a Calendar instance based on its milliseconds
     * @param millis long value representing the time in milliseconds
     * @return Returns the Calendar instance based on the milliseconds input.
     */
    public static Calendar calendarFromMillis(long millis) {
        Calendar calendar = Calendar.getInstance(java.util.Locale.getDefault());
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    public static Calendar now() {
        return Calendar.getInstance(java.util.Locale.getDefault());
    }

}
