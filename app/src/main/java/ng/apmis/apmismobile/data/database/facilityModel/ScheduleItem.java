package ng.apmis.apmismobile.data.database.facilityModel;

/**
 * Individual schedule rolled out by a <code>Clinic</code>
 */
public class ScheduleItem {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Start time of the appointment Schedule
     */
    private String startTime;

    /**
     * End time of the appointment Schedule
     */
    private String endTime;

    /**
     * Day of the week in which the appointment is scheduled
     */
    private String day;


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

    /**
     * Assigns an integer for each day of the week
     * @return the integer assigned for the weekday
     */
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
