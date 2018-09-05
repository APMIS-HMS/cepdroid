package ng.apmis.apmismobile.data.database.facilityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ng.apmis.apmismobile.utilities.AppUtils;

public class ScheduleItem {


    private String _id;
    private String endTime;
    private String startTime;
    private String day;
    private String updatedAt;
    private String createdAt;



    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDayAsInteger(){

        switch (getDay()){
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;
            default:
                return 2;
        }
    }

}
