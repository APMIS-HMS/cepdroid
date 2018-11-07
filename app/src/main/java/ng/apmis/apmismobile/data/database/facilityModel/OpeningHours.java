package ng.apmis.apmismobile.data.database.facilityModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpeningHours {

    @SerializedName("weekday_text")
    private List<String> weekdayText = null;
    private List<Period> periods = null;

    @SerializedName("open_now")
    private Boolean openNow;

    public List<String> getWeekdayText() {
        return weekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        this.weekdayText = weekdayText;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }


    static class Period {

        private Open open;
        private Close close;

        public Open getOpen() {
            return open;
        }

        public void setOpen(Open open) {
            this.open = open;
        }

        public Close getClose() {
            return close;
        }

        public void setClose(Close close) {
            this.close = close;
        }

        static class Open {

            private Long nextDate;
            private Integer minutes;
            private Integer hours;
            private String time;
            private Integer day;

            public Long getNextDate() {
                return nextDate;
            }

            public void setNextDate(Long nextDate) {
                this.nextDate = nextDate;
            }

            public Integer getMinutes() {
                return minutes;
            }

            public void setMinutes(Integer minutes) {
                this.minutes = minutes;
            }

            public Integer getHours() {
                return hours;
            }

            public void setHours(Integer hours) {
                this.hours = hours;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

        }

        static class Close {

            private Long nextDate;
            private Integer minutes;
            private Integer hours;
            private String time;
            private Integer day;

            public Long getNextDate() {
                return nextDate;
            }

            public void setNextDate(Long nextDate) {
                this.nextDate = nextDate;
            }

            public Integer getMinutes() {
                return minutes;
            }

            public void setMinutes(Integer minutes) {
                this.minutes = minutes;
            }

            public Integer getHours() {
                return hours;
            }

            public void setHours(Integer hours) {
                this.hours = hours;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

        }

    }

}
