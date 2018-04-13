package ng.apmis.apmismobile.data;


import android.arch.lifecycle.LiveData;
import android.util.Log;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.database.ApmisDao;
import ng.apmis.apmismobile.data.database.model.PersonEntry;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;

/**
 * Source of truth for data whether from database or from server
 * First serves data from room database with livedata
 */

public class ApmisRepository {

    private static final String LOG_TAG = ApmisRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static ApmisRepository sInstance;
    private final ApmisDao mWeatherDao;
    private final ApmisNetworkDataSource mApmisNetworkDataSource;
    private final APMISAPP mExecutors;
    private boolean mInitialized = false;

    private ApmisRepository(ApmisDao apmisDao,
                               ApmisNetworkDataSource apmisNetworkDataSource,
                               APMISAPP executors) {
        mWeatherDao = apmisDao;
        mApmisNetworkDataSource = apmisNetworkDataSource;
        mExecutors = executors;
        LiveData<PersonEntry[]> networkData = mApmisNetworkDataSource.getCurrentPersonData();

        networkData.observeForever(newpersonData -> {
            mExecutors.diskIO().execute(() -> {
                // Insert our new weather data into Sunshine's database
                deleteOldData();
                Log.d(LOG_TAG, "Old person detail deleted");
                // Insert our new weather data into Sunshine's database
                mWeatherDao.insertData(newpersonData);
                Log.d(LOG_TAG, "New values inserted");
            });
        });

    }

    public synchronized static ApmisRepository getsInstance (ApmisDao apmisDao, ApmisNetworkDataSource apmisNetworkDataSource, APMISAPP executorThreads) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ApmisRepository(apmisDao, apmisNetworkDataSource,
                        executorThreads);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    public synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded()) {
                startFetchPersonDataService();
            }
        });
    }

    public LiveData<PersonEntry> getUserData () {
        initializeData();
        return mWeatherDao.getUserData();
    }


    /**
     * Remove all old person data to allow new update
     */
    private void deleteOldData () {
        mWeatherDao.deleteOldData();
    }

    //TODO consider condition to check before fetching data from server
    private boolean isFetchNeeded() {
        return true;
    }

    private void startFetchPersonDataService() {
        mApmisNetworkDataSource.startPersonDataFetchService();
    }


}
