package ng.apmis.apmismobile.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;


/**
 * This Interface is for data access to the apmis in room database
 * All database queries for details of the person are specified here
 */


@Dao
public interface ApmisDao {

    @Query("SELECT * FROM person LIMIT 1")
    LiveData<PersonEntry> getUserData();

    @Query("SELECT * FROM person LIMIT 1")
    PersonEntry getStaticUserData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(PersonEntry... personEntries);

    @Query("DELETE FROM person")
    void deleteOldData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE personId = :personId ORDER BY startDate ASC")
    LiveData<List<Appointment>> findAppointmentsForPerson(String personId);

    @Query("SELECT * FROM appointments WHERE _id = :appointment_id")
    LiveData<Appointment> findAppointmentById(String appointment_id);

    @Query("SELECT * FROM appointments WHERE _id = :appointment_id")
    Appointment getAppointmentById(String appointment_id);

    @Query("SELECT * FROM appointments WHERE personId = :personId AND (startDate LIKE :today OR startDate LIKE :yesterday OR startDate LIKE :tomorrow) ORDER BY startDate ASC")
    LiveData<List<Appointment>> getDailyAppointments(String personId, String today, String yesterday, String tomorrow);

    @Query("DELETE FROM appointments")
    void deleteAllAppointments();

}
