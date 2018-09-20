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
 * Created by mofejegi-apmis on 9/5/2018.
 */

public class AppUtils {

    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    public static String localDateToDbString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String dateString;

        dateString = format.format(date);

        return dateString;
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

    public static String dateToReadableFullDateString(Date date){
        String pattern = "EEE, MMM dd, yyyy 'at' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    public static String dateToReadableDateString(Date date){
        String pattern = "EEE, MMM dd, yyyy 'at' hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    public static String dateToShortDateString(Date date){
        String pattern = "dd/MM/yyyy";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    public static String dateToVeryShortDateString(Date date){
        String pattern = "dd/MM/yy";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    public static String dateToReadableTimeString(Date date){
        String pattern = "hh:mm a";

        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);

        return format.format(date);
    }

    /**
     * @return
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
                if (now.after(getTodayWithScheduledTime(scheduleItem.getEndTime(), 0, date))){
                    return getNextScheduleTimeLimits(scheduleItem, true, date);
                }
            }

        } else {
            return getNextScheduleTimeLimits(scheduleItem, true, date);
        }

        int offsetDays = weekDaySchedule-weekDayToday;

        return new Date[]{
                getTodayWithScheduledTime(scheduleItem.getStartTime(), offsetDays, date),
                getTodayWithScheduledTime(scheduleItem.getEndTime(), offsetDays, date)
        };
    }

    private static Date getTodayWithScheduledTime(String dateString, int offsetDays, Date date){
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
        //Offset days if a day in the future is required
        cal.add(Calendar.DAY_OF_YEAR, offsetDays);

        return cal.getTime();
    }


    public static boolean isInPossibleScheduleTimeLimit(ScheduleItem scheduleItem, Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Date dates[] = AppUtils.getNextScheduleTimeLimits(scheduleItem, false, date);

        Date start = dates[0];
        Date end = dates[1];

        return !(cal.getTime().before(start) || cal.getTime().after(end));
    }

    public static void showShortToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static float doubleToFloat(Double d){
        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.floatValue();
    }



}
