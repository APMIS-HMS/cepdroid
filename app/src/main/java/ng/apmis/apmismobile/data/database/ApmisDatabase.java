package ng.apmis.apmismobile.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import ng.apmis.apmismobile.data.database.model.Converters;
import ng.apmis.apmismobile.data.database.model.PersonEntry;

/**
 * This class takes care of creating the person database (ROOM)
 */

@Database(entities = {PersonEntry.class}, version = 1)
@TypeConverters({Converters.class})

public abstract class ApmisDatabase extends RoomDatabase {

    public abstract ApmisDao personDao();

    private static final String DATABASE_NAME = "apmis";

    private static final Object LOCK = new Object();

    private static volatile ApmisDatabase sInstance;

    public static ApmisDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), ApmisDatabase.class, ApmisDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

}
