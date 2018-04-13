package ng.apmis.apmismobile.utilities;

import android.content.Context;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.ApmisDatabase;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;


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

}
