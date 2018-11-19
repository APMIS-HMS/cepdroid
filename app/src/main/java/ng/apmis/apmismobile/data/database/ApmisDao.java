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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(PersonEntry... personEntries);

    @Query("DELETE FROM person")
    void deleteOldData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAppointment(Appointment appointment);

    @Query("SELECT * FROM appointments WHERE personId = :patientId ORDER BY startDate DESC")
    LiveData<List<Appointment>> findAppointmentsForPatient(String patientId);

    @Query("SELECT * FROM appointments WHERE _id = :appointment_id")
    LiveData<Appointment> findAppointmentById(String appointment_id);

    @Query("SELECT * FROM appointments WHERE _id = :appointment_id")
    Appointment getAppointmentById(String appointment_id);

    @Query("DELETE FROM appointments")
    void deleteAllAppointments();

}
