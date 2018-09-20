package ng.apmis.apmismobile;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ng.apmis.apmismobile.data.database.facilityModel.ScheduleItem;
import ng.apmis.apmismobile.utilities.AppUtils;

@RunWith(AndroidJUnit4.class)
public class DateConversionTests {

    private static final String TAG = "DateConversionTests";

    @Test
    public void seeTimeZone(){
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String dateString = "2018-03-08T03:00:52.739Z";

        try {
            Log.i(TAG, "UTC enabled:" + format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void seeTimeZoneNoUtc(){
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

        String dateString = "2018-03-08T03:00:52.739Z";

        try {
            Log.i(TAG, "No UTC enabled:" + format.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void seeDBTimeZone(){
//        SimpleDateFormat format = new SimpleDateFormat(
//                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//        format.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//        Date now = new Date();
//
//        Log.i(TAG, "UTC enabled DB:" + format.format(now));
//    }
//
//    @Test
//    public void seeDBTimeZoneNoUtc(){
//        SimpleDateFormat format = new SimpleDateFormat(
//                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
//
//        Date now = new Date();
//
//        Log.i(TAG, "No UTC enabled DB:" + format.format(now));
//    }

    @Test
    public void testDateLimits(){
        ScheduleItem scheduleItem = new ScheduleItem();
        scheduleItem.setStartTime("2018-02-06T06:00:00.000Z");
        scheduleItem.setEndTime("2018-02-06T20:00:00.000Z");

        Date[] arr = AppUtils.getNextScheduleTimeLimits(scheduleItem, false, null);

        Log.i(TAG, arr[0].toString());
        Log.i(TAG, arr[1].toString());
    }

}
