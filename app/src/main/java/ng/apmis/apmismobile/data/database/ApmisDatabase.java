package ng.apmis.apmismobile.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.model.Converters;
import ng.apmis.apmismobile.data.database.model.PersonEntry;

/**
 * This class takes care of creating the person database (ROOM)
 */

@Database(entities = {PersonEntry.class, Appointment.class}, version = 2)
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
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            ApmisDatabase.class, ApmisDatabase.DATABASE_NAME)
                            //.addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return sInstance;
    }

    //NOT NEEDED AGAIN (for now)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `appointments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `facilityName` TEXT, `_id` TEXT, `orderStatusId` TEXT, `category` TEXT, `startDate` TEXT, `patientId` TEXT, `facilityId` TEXT, `doctorId` TEXT, `clinicId` TEXT, `appointmentTypeId` TEXT, `appointmentReason` TEXT, `isActive` INTEGER, `age` TEXT)");
            database.execSQL("CREATE UNIQUE INDEX `index_appointments__id` ON `appointments` (`_id`)");
        }
    };

}
