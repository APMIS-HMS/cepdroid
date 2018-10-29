package ng.apmis.apmismobile.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;

/**
 * This class takes care of creating the person database (ROOM)
 */

@Database(entities = {PersonEntry.class, Appointment.class}, version = 6)
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
                            .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
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

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `person_backup` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `apmisId` TEXT, `title` TEXT, `firstName` TEXT, `lastName` TEXT, `gender` TEXT, `motherMaidenName` TEXT, `securityQuestion` TEXT, `securityAnswer` TEXT, `primaryContactPhoneNo` TEXT, `secondaryContactPhoneNo` TEXT, `dateOfBirth` TEXT, `email` TEXT, `otherNames` TEXT, `biometric` TEXT, `personProfessions` TEXT, `nationality` TEXT, `stateOfOrigin` TEXT, `lgaOfOrigin` TEXT, `profileImageUri` TEXT, `homeAddress` TEXT, `maritalStatus` TEXT, `nextOfKin` TEXT, `wallet` TEXT)");
            database.execSQL("INSERT INTO `person_backup` (`id`, `apmisId`, `title`, `firstName`, `lastName`, `gender`, `motherMaidenName`, `securityQuestion`, `securityAnswer`, `primaryContactPhoneNo`, `secondaryContactPhoneNo`, `dateOfBirth`, `email`, `otherNames`, `biometric`, `personProfessions`, `nationality`, `stateOfOrigin`, `lgaOfOrigin`, `homeAddress`, `maritalStatus`, `nextOfKin`, `wallet`) SELECT `id`, `apmisId`, `title`, `firstName`, `lastName`, `gender`, `motherMaidenName`, `securityQuestion`, `securityAnswer`, `primaryContactPhoneNo`, `secondaryContactPhoneNo`, `dateOfBirth`, `email`, `otherNames`, `biometric`, `personProfessions`, `nationality`, `stateOfOrigin`, `lgaOfOrigin`, `homeAddress`, `maritalStatus`, `nextOfKin`, `wallet` FROM `person`");
            database.execSQL("DROP TABLE `person`");
            database.execSQL("ALTER TABLE `person_backup` RENAME TO `person`");
            database.execSQL("CREATE UNIQUE INDEX `index_person_apmisId` ON `person` (`apmisId`)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `person_backup` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `apmisId` TEXT, `title` TEXT, `firstName` TEXT, `lastName` TEXT, `gender` TEXT, `motherMaidenName` TEXT, `securityQuestion` TEXT, `securityAnswer` TEXT, `primaryContactPhoneNo` TEXT, `secondaryContactPhoneNo` TEXT, `dateOfBirth` TEXT, `email` TEXT, `otherNames` TEXT, `biometric` TEXT, `personProfessions` TEXT, `nationality` TEXT, `stateOfOrigin` TEXT, `lgaOfOrigin` TEXT, `profileImageUriPath` TEXT, `homeAddress` TEXT, `maritalStatus` TEXT, `nextOfKin` TEXT, `wallet` TEXT)");
            database.execSQL("INSERT INTO `person_backup` (`id`, `apmisId`, `title`, `firstName`, `lastName`, `gender`, `motherMaidenName`, `securityQuestion`, `securityAnswer`, `primaryContactPhoneNo`, `secondaryContactPhoneNo`, `dateOfBirth`, `email`, `otherNames`, `biometric`, `personProfessions`, `nationality`, `stateOfOrigin`, `lgaOfOrigin`, `homeAddress`, `maritalStatus`, `nextOfKin`, `wallet`) SELECT `id`, `apmisId`, `title`, `firstName`, `lastName`, `gender`, `motherMaidenName`, `securityQuestion`, `securityAnswer`, `primaryContactPhoneNo`, `secondaryContactPhoneNo`, `dateOfBirth`, `email`, `otherNames`, `biometric`, `personProfessions`, `nationality`, `stateOfOrigin`, `lgaOfOrigin`, `homeAddress`, `maritalStatus`, `nextOfKin`, `wallet` FROM `person`");
            database.execSQL("DROP TABLE `person`");
            database.execSQL("ALTER TABLE `person_backup` RENAME TO `person`");
            database.execSQL("CREATE UNIQUE INDEX `index_person_apmisId` ON `person` (`apmisId`)");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //add the ProfileImageFileName column to the Person table
            database.execSQL("ALTER TABLE `person` ADD COLUMN `profileImageFileName` TEXT");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //add the ProfileImageFileName column to the Person table
            database.execSQL("ALTER TABLE `person` ADD COLUMN `_id` TEXT");
        }
    };



}
