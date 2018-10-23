package ng.apmis.apmismobile.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    /**
     *
     * @param bitmap
     * @return
     */
    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, buffer);
        return buffer.toByteArray();
    }

    /**
     *
     * @param byteArray
     * @return
     */
    public static Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        return bmp;
    }

    /**
     * Check if user is connected to the internet
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Helper method to copy contents of one file into another
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static String compressImage(Context context, String imagePath, int compressTries) {

        String filePath = imagePath;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 1000x1000

        float maxHeight = 500.0f - (compressTries*100);
        float maxWidth = 500.0f - (compressTries*100);
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            //Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                //Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                //Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                //Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imagePath; //overwrite the original image
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //compress the image if it's above 50KB or if it hasn't compressed a fifth time yet
        long length = new File(imagePath).length()/1024;
        if (length > 50 && compressTries < 4){
            return compressImage(context, filename, compressTries+1);
        }
        else
            return filename;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


}
