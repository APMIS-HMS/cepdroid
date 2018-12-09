package ng.apmis.apmismobile.ui.dashboard.find.foundItems;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.SearchTermItem;

public class FoundItemsViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<List<SearchTermItem>> foundItems;

    FoundItemsViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;
    }

    public LiveData<List<SearchTermItem>> getFoundItems(String itemType, String searchQuery){
        foundItems = apmisRepository.getNetworkDataSource().getFoundObjects(itemType, searchQuery, 0);
        return foundItems;
    }

    public void populateExtra(String itemType, String searchQuery, int skipCount){
        apmisRepository.getNetworkDataSource().getFoundObjects(itemType, searchQuery, skipCount);
        Log.e("Find", "Skipped "+skipCount);
    }


    public void clearFoundItems(){
        apmisRepository.getNetworkDataSource().clearFoundObjects();
    }
}
