package ng.apmis.apmismobile.utilities;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;

/**
 * Created by mofeejegi-apmis on 9/5/2018.
 * Utility class for handling some minor functions
 */

public class AppUtils {

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    /**
     * Converts the current date-time to the String format required by the APMIS database.
     * Also converts timezone from the local TimeZone to UTC.
     * @param date {@link Date} object to convert
     * @return String formatted in the APMIS Db style
     */
    public static String localDateToDbString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String dateString;

        dateString = format.format(date);

        return dateString;
    }

    /**
     * Converts the date string received from the APMIS DB to a Date item
     * Also converts timezone from UTC to the local timezone
     * @param dateString Date string in APMIS data
     * @return The {@link Date} item in the current timezone
     */
    public static Date dbStringToLocalDate(String dateString){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Formats a date to a readable string, <br/>
     * e.g, 2018-09-26 09:00:00 date would be represented as Wed, Sep 26, 2018 at 09:00 am
     * @param date The {@link Date} object to format
     * @return Readable date string
     */
    public static String dateToReadableFullDateString(Date date){
        String pattern = "EEE, MMM dd, yyyy 'at' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * Formats a date to a short readable string, <br/>
     * e.g, 2018-09-26 09:00:00 date would be represented as 26/09/2018
     * according to the UK locale convention
     * @param date The {@link Date} object to format
     * @return Short readable date string
     */
    public static String dateToShortDateString(Date date){
        String pattern = "dd/MM/yyyy";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * Formats a date to a very short readable string, <br/>
     * e.g, 2018-09-26 09:00:00 date would be represented as 26/09/18
     * according to the UK locale convention
     * @param date The {@link Date} object to format
     * @return Very short readable date string
     */
    public static String dateToVeryShortDateString(Date date){
        String pattern = "dd/MM/yy";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * Formats a date to a short readable time string, <br/>
     * e.g, 2018-09-26 09:00:00 date would be represented as 09:00 am
     * ignoring the date values
     * @param date The {@link Date} object to format
     * @return Short readable time string
     */
    public static String dateToReadableTimeString(Date date){
        String pattern = "hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * Convenience method to return Upper and lower date limits for a weekday schedule in a time period
     * @param scheduleItem The ScheduleItem from the Clinic
     * @param isNextWeek Recursive check to see if the Scheduled weekday has passed this current week and falls into the next week
     * @param date The Date to check with
     * @return The Upper and Lower Date limits in which the ScheduleItem falls into
     */
    public static Date[] getNextScheduleTimeLimits(ScheduleItem scheduleItem, boolean isNextWeek, Date date){
        //Get today's calendar date
        Calendar cal = Calendar.getInstance();
        //Update to a preset date, if available
        if (date != null)
            cal.setTime(date);

        int weekDaySchedule = scheduleItem.getDayAsInteger();

        //get the day in the week today
        int weekDayToday = cal.get(Calendar.DAY_OF_WEEK);

        //Push the schedule up till the next weekday
        if (isNextWeek) {
            weekDaySchedule = weekDaySchedule + 7;
            cal.add(Calendar.DAY_OF_YEAR, ((weekDaySchedule) - weekDayToday));
        }

        //if the day of the week hasn't passed yet...
        if (weekDayToday <= weekDaySchedule){

            //next do a check for of today is the day of the week
            if (weekDayToday == weekDaySchedule){
                //if it is, then check if end time has passed
                Date now = new Date();
                //if it has passed, re run method using next week
                if (now.after(getDateWithScheduledTime(scheduleItem.getEndTime(), 0, date))){
                    return getNextScheduleTimeLimits(scheduleItem, true, date);
                }//otherwise, continue
            }

        } else {
            //re run method using next week
            return getNextScheduleTimeLimits(scheduleItem, true, date);
        }

        //the days between today(or the date in view) and the next scheduled weekday
        int offsetDays = weekDaySchedule-weekDayToday;

        return new Date[]{
                getDateWithScheduledTime(scheduleItem.getStartTime(), offsetDays, date),
                getDateWithScheduledTime(scheduleItem.getEndTime(), offsetDays, date)
        };
    }

    /**
     * Simply removes the time part from a date object
     * @param dateString The db date string from which we take the time period from
     * @param offsetDays The days between today(or the date in view) and the next scheduled weekday
     *                   with which an offset would be added
     * @param date The date to check with and strip it's time part away
     * @return The new Date object with the new time
     */
    private static Date getDateWithScheduledTime(String dateString, int offsetDays, Date date){
        Date dbDate = dbStringToLocalDate(dateString);

        SimpleDateFormat hours = new SimpleDateFormat("HH", Locale.UK);
        SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.UK);
        SimpleDateFormat seconds = new SimpleDateFormat("ss", Locale.UK);

        //Get today's calendar date @ 00:00
        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //then add schedule time
        int hour = Integer.parseInt(hours.format(dbDate));
        int minute = Integer.parseInt(minutes.format(dbDate));
        int second = Integer.parseInt(seconds.format(dbDate));

        cal.clear();
        cal.set(year, month, day, hour, minute, 0);
        //Offset days if a future day is required
        cal.add(Calendar.DAY_OF_YEAR, offsetDays);

        return cal.getTime();
    }


    /**
     * Convenience method to ensure that a Date picked falls within a particular time frame in a particular week day
     * @param scheduleItem The ScheduleItem with the time range
     * @param date The date to check with
     * @return True if it falls within, false otherwise
     */
    public static boolean isInPossibleScheduleTimeLimit(ScheduleItem scheduleItem, Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Date dates[] = AppUtils.getNextScheduleTimeLimits(scheduleItem, false, date);

        Date start = dates[0];
        Date end = dates[1];

        return !(cal.getTime().before(start) || cal.getTime().after(end));
    }

    /**
     * Convenience method to display a short toast
     * @param context The calling context
     * @param message The toast message
     */
    public static void showShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Converts a Double type to a Float type
     * @param d The double to convert
     * @return The converted float
     */
    public static float doubleToFloat(Double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.floatValue();
    }



}
