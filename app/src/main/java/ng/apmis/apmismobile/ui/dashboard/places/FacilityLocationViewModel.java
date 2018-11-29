package ng.apmis.apmismobile.ui.dashboard.places;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;

/**
 * Created by Thadeus-APMIS on 11/29/2018.
 */

public class FacilityLocationViewModel extends ViewModel {

    ApmisRepository apmisRepository;

    FacilityLocationViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
    }

    public LiveData<Facility[]> getFacilityLocations () {
        return apmisRepository.getNetworkDataSource().getNearbyLocations();
    }

}
