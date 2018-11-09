package ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.personModel.Wallet;

public class FoundHospitalDetailViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<Facility> facility;
    private LiveData<String> serviceCategoryId;
    private LiveData<BillManager> billManager;
    private LiveData<Wallet> wallet;

    FoundHospitalDetailViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }

    public LiveData<Facility> getFacility(String facilityId) {
        facility = apmisRepository.getNetworkDataSource().getFacilityDetails(facilityId);
        return facility;
    }

    public LiveData<String> getServiceCategoryIdForFacility(String facilityId) {
        serviceCategoryId = apmisRepository.getNetworkDataSource().getServiceCategoryIdForFacility(facilityId);
        return serviceCategoryId;
    }

    public LiveData<BillManager> getBillManagerForRegistration(String facilityId, String categoryId) {
        billManager = apmisRepository.getNetworkDataSource().getBillManagerForFacilityServiceCategory(facilityId, categoryId);
        return billManager;
    }

    public LiveData<Wallet> getPersonWallet(String personId) {
        wallet = apmisRepository.getNetworkDataSource().getPersonWallet(personId);
        return wallet;
    }

    public void clearFacilityData(){
        apmisRepository.getNetworkDataSource().clearFacilityData();
    }

    public void clearServiceCategoryId(){
        apmisRepository.getNetworkDataSource().clearServiceCategoryId();
    }

    public void clearBills(){
        apmisRepository.getNetworkDataSource().clearBillManager();
    }

    public void clearPersonWallet(){
        apmisRepository.getNetworkDataSource().clearWallet();
    }
}
