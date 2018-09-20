package ng.apmis.apmismobile.utilities;

import android.content.Context;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.ApmisDatabase;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;
import ng.apmis.apmismobile.ui.dashboard.PersonFactory;
import ng.apmis.apmismobile.ui.dashboard.appointment.AddAppointmentViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.appointment.AppointmentViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.documentation.RecordsListViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.healthProfile.HealthProfileViewModelFactory;


/**
 * Provides static method for injection (Satisfies all dependencies to use network and room data
 */

public class InjectorUtils {

    /**
     * Injects all dependencies to get database data or network data
     * @param context
     * @return
     */
    public static ApmisRepository provideRepository (Context context) {
        ApmisDatabase apmisDatabase = ApmisDatabase.getsInstance(context.getApplicationContext());
        APMISAPP executorThreads = APMISAPP.getInstance();
        ApmisNetworkDataSource apmisNetworkDataSource = ApmisNetworkDataSource.getsInstance(context, executorThreads);
        return ApmisRepository.getsInstance(apmisDatabase.personDao(), apmisNetworkDataSource, executorThreads);
    }

    /**
     * Injects all dependencies to get network data
     * @param context
     * @return
     */
    public static ApmisNetworkDataSource provideNetworkData (Context context) {
        APMISAPP executorThreads = APMISAPP.getInstance();
        return ApmisNetworkDataSource.getsInstance(context, executorThreads);
    }

    /**
     * Injects all dependencies to provide local data
     * @param context
     * @return
     */
    public static PersonFactory providePersonFactory (Context context) {
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new PersonFactory(apmisRepository);
    }

    public static AddAppointmentViewModelFactory provideAddAppointmentViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new AddAppointmentViewModelFactory(apmisRepository);
    }

    public static AppointmentViewModelFactory provideAppointmentViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new AppointmentViewModelFactory(apmisRepository, context);
    }

    public static RecordsListViewModelFactory provideRecordsViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new RecordsListViewModelFactory(apmisRepository, context);
    }

    public static HealthProfileViewModelFactory provideHealthProfileViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new HealthProfileViewModelFactory(apmisRepository, context);
    }


}
