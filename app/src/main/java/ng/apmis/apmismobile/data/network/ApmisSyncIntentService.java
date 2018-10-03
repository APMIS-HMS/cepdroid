package ng.apmis.apmismobile.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import ng.apmis.apmismobile.utilities.InjectorUtils;

public class ApmisSyncIntentService extends IntentService {

    private static final String LOG_TAG = ApmisSyncIntentService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ApmisSyncIntentService() {
        super("ApmisSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "Network Intent service started");
        ApmisNetworkDataSource apmisNetworkDataSource = InjectorUtils.provideNetworkData(getApplicationContext());
        apmisNetworkDataSource.fetchPersonDetails();
    }
}
