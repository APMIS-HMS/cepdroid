package ng.apmis.apmismobile.data.database.appointmentModel;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

public class Appointments {

    private String facilityName;
    private String clinicName;
    private long appointmentDateTime;

    public Appointments(String facilityName, String clinicName, long appointmentDateTime) {
        this.facilityName = facilityName;
        this.clinicName = clinicName;
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public long getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(long appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "facilityName='" + facilityName + '\'' +
                ", clinicName='" + clinicName + '\'' +
                ", appointmentDateTime=" + appointmentDateTime +
                '}';
    }
}
