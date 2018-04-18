package ng.apmis.apmismobile.data.database.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date todate (Long timeStamp) {
        return timeStamp == null ? null : new Date(timeStamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Use this conversion to store {SecondaryContactNo}
     * @param varies
     * @return
     */
    @TypeConverter
    public static String arrayToString (String [] varies) {
        StringBuilder valueToStore = new StringBuilder();
        valueToStore.append("[");
        for (int i = 0; i < varies.length; i++) {
            if (i != varies.length - 1) {
                valueToStore.append(varies[i] + ",");
            } else {
                valueToStore.append(varies[1]);
            }
        }
        valueToStore.append("]");

        return valueToStore.toString();
    }

}
