package ng.apmis.apmismobile.utilities;

import android.content.Context;

import ng.apmis.apmismobile.APMISAPP;
import ng.apmis.apmismobile.data.ApmisRepository;
import ng.apmis.apmismobile.data.database.ApmisDatabase;
import ng.apmis.apmismobile.data.database.appointmentModel.Appointment;
import ng.apmis.apmismobile.data.database.diagnosesModel.LabRequest;
import ng.apmis.apmismobile.data.database.documentationModel.Documentation;
import ng.apmis.apmismobile.data.database.prescriptionModel.Prescription;
import ng.apmis.apmismobile.data.network.ApmisNetworkDataSource;
import ng.apmis.apmismobile.ui.dashboard.PersonFactory;
import ng.apmis.apmismobile.ui.dashboard.appointment.AddAppointmentViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.appointment.AppointmentViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.diagnoses.DiagnosisViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.documentation.RecordsListViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.find.PreviousItemsViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.healthProfile.HealthProfileViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.prescription.PrescriptionViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.profile.profileAction.ProfileActionViewModelFactory;
import ng.apmis.apmismobile.ui.dashboard.profile.viewEditProfile.EditProfileViewModelFactory;


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
        return new PersonFactory(context, apmisRepository);
    }

    /**
     * Injects all dependencies to provide local or network data for {@link Appointment}s
     * @param context
     * @return
     */
    public static AddAppointmentViewModelFactory provideAddAppointmentViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new AddAppointmentViewModelFactory(apmisRepository);
    }

    /**
     * Injects all dependencies to provide local or network data for {@link Appointment}s
     * @param context
     * @return
     */
    public static AppointmentViewModelFactory provideAppointmentViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new AppointmentViewModelFactory(apmisRepository, context);
    }

    /**
     * Injects all dependencies to provide local or network data for {@link Documentation}s
     * @param context
     * @return
     */
    public static RecordsListViewModelFactory provideRecordsViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new RecordsListViewModelFactory(apmisRepository, context);
    }

    /**
     * Injects all dependencies to provide local or network data for {@link Documentation}s
     * @param context
     * @return
     */
    public static HealthProfileViewModelFactory provideHealthProfileViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new HealthProfileViewModelFactory(apmisRepository, context);
    }

    /**
     * Injects all dependencies to provide network data for {@link Prescription}s
     * @param context
     * @return
     */
    public static PrescriptionViewModelFactory providePrescriptionsViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new PrescriptionViewModelFactory(apmisRepository);
    }

    /**
     * Injects all dependencies to provide network data for {@link LabRequest}s
     * @param context
     * @return
     */
    public static DiagnosisViewModelFactory provideDiagonosisViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new DiagnosisViewModelFactory(apmisRepository);
    }

    /**
     *
     * @param context
     * @return
     */
    public static EditProfileViewModelFactory provideEditProfileViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new EditProfileViewModelFactory(context, apmisRepository);
    }

    /**
     *
     * @param context
     * @return
     */
    public static ProfileActionViewModelFactory provideProfileActionViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new ProfileActionViewModelFactory(context, apmisRepository);
    }

    /**
     *
     * @param context
     * @return
     */
    public static PreviousItemsViewModelFactory providePreviousItemsViewModelFactory(Context context){
        ApmisRepository apmisRepository = provideRepository(context.getApplicationContext());
        return new PreviousItemsViewModelFactory(context, apmisRepository);
    }

}
