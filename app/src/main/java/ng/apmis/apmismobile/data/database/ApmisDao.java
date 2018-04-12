package ng.apmis.apmismobile.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import ng.apmis.apmismobile.data.database.model.PersonEntry;


/**
 * This Interface is for data access to the apmis in room database
 * All database queries for details of the person are specified here
 */


@Dao
public interface ApmisDao {

    @Query("SELECT * FROM person")
    LiveData<PersonEntry> getUser();

}
