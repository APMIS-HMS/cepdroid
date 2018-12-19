package ng.apmis.apmismobile.ui.dashboard.find.foundItems.foundHospital;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.List;

import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.HMO;
import ng.apmis.apmismobile.data.database.fundAccount.BillManager;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.data.database.personModel.Wallet;

public class FoundHospitalDetailViewModel extends ViewModel {

    private ApmisRepository apmisRepository;
    private LiveData<Facility> facility;
    private LiveData<List<String>> facilityServiceAndCategoryIds;
    private LiveData<BillManager> billManager;
    private LiveData<Wallet> wallet;

    FoundHospitalDetailViewModel(ApmisRepository apmisRepository) {
        this.apmisRepository = apmisRepository;

    }

    public LiveData<Facility> getFacility(String facilityId) {
        facility = apmisRepository.getNetworkDataSource().getFacilityDetails(facilityId);
        return facility;
    }

    public LiveData<List<String>> getFacilityServiceCategoryIdsForFacility(String facilityId) {
        facilityServiceAndCategoryIds = apmisRepository.getNetworkDataSource().getServiceCategoryIdForFacility(facilityId);
        return facilityServiceAndCategoryIds;
    }

    public LiveData<BillManager> getBillManagerForRegistration(String facilityId, String categoryId) {
        billManager = apmisRepository.getNetworkDataSource().getBillManagerForFacilityServiceCategory(facilityId, categoryId);
        return billManager;
    }

    public LiveData<Wallet> getPersonWallet(String personId) {
        wallet = apmisRepository.getNetworkDataSource().getPersonWallet(personId, true);
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

    public void clearPatientOnRegistration(){
        apmisRepository.getNetworkDataSource().clearPatientOnRegistration();
    }

    public LiveData<Patient> registerPatient(String personId, String facilityId, String coverType, int cost,
                                             int amountPaid, String facilityServiceId, String registrationCategoryId,
                                             String serviceId, String category, String service, JSONObject coverObject){

        return apmisRepository.getNetworkDataSource().registerPatient(personId, facilityId, coverType, cost,
                amountPaid, facilityServiceId, registrationCategoryId, serviceId, category, service, coverObject);
    }

    public LiveData<List<String>> getRegisteredFacilityIds() {
        return apmisRepository.getNetworkDataSource().getRegisteredFacilityIds();
    }

    public void clearFacilityHMOS(){
        apmisRepository.getNetworkDataSource().clearFetchedFacilityHMOS();
    }

    public LiveData<List<HMO>> getFacilityHmos(String facilityId){
        return apmisRepository.getNetworkDataSource().getHMOSInFacility(facilityId);
    }
}
