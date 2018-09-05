package ng.apmis.apmismobile.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;

public class AppUtils {

    public static String dateToDbString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String dateString;

        dateString = format.format(date);

        return dateString;
    }

    public static Date dbStringToUTCDate(String dateString){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        Date date = null;

        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

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

    public static String dateToReadableString(Date date){
        String pattern = "EEE, MMM dd, yyyy 'at' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    public static String timeToReadableString(Date date){
        String pattern = "hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * @return
     */
    public static Date[] getNextScheduleTimeLimits(ScheduleItem scheduleItem, boolean isNextWeek){
        //Get today's calendar date
        Calendar cal = Calendar.getInstance();

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
                if (now.after(getTodayWithScheduledTime(scheduleItem.getEndTime(), 0))){
                    return getNextScheduleTimeLimits(scheduleItem, true);
                }
            }

        } else {
            return getNextScheduleTimeLimits(scheduleItem, true);
        }

        int offsetDays = weekDaySchedule-weekDayToday;

        return new Date[]{
                getTodayWithScheduledTime(scheduleItem.getStartTime(), offsetDays),
                getTodayWithScheduledTime(scheduleItem.getEndTime(), offsetDays)
        };
    }


    public static boolean isInPossibleScheduleTimeLimit(ScheduleItem scheduleItem){

        int weekDaySchedule = scheduleItem.getDayAsInteger();




        return true;
    }


    private static Date getTodayWithScheduledTime(String dateString, int offsetDays){
        Date date = dbStringToLocalDate(dateString);

        SimpleDateFormat hours = new SimpleDateFormat("HH", Locale.UK);
        SimpleDateFormat minutes = new SimpleDateFormat("mm", Locale.UK);
        SimpleDateFormat seconds = new SimpleDateFormat("ss", Locale.UK);

        //Get today's calendar date @ 00:00
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //then add schedule time
        int hour = Integer.parseInt(hours.format(date));
        int minute = Integer.parseInt(minutes.format(date));
        int second = Integer.parseInt(seconds.format(date));

        cal.clear();
        cal.set(year, month, day, hour, minute, 0);
        //Offset days if a day in the future is required
        cal.add(Calendar.DAY_OF_YEAR, offsetDays);

        return cal.getTime();
    }

}
