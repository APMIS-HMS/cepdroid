package ng.apmis.apmismobile.data.database.appointmentModel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ng.apmis.apmismobile.data.database.facilityModel.Clinic;
import ng.apmis.apmismobile.data.database.facilityModel.Employee;
import ng.apmis.apmismobile.data.database.facilityModel.Facility;
import ng.apmis.apmismobile.data.database.facilityModel.Service;
import ng.apmis.apmismobile.data.database.personModel.PersonEntry;
import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018. <br/>
 * Model class representing appointments which
 * carry details of a schedule between doctor and patient.
 * Class is also a {@link Room} database entity
 */

@Entity(tableName = "appointments", indices = {@Index(value = {"_id"}, unique = true)})
public class Appointment implements Comparable<Appointment>{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String _id;
    @Ignore
    private String createdAt;
    @Ignore
    private String updatedAt;

    /**
     * Registered Name of the {@link Facility}
     */
    private String facilityName;
    private String orderStatusId;

    /**
     * The {@link Service} category name offered/rendered by the {@link Clinic}
     */
    private String category;

    /**
     * Start Date of the Scheduled appointment
     */
    private String startDate;

    /**
     * Unique Person Id for the {@link PersonEntry} who created this appointment
     */
    private String personId;

    /**
     * Unique Patient Id for the {@link Patient} who created this appointment
     */
    private String patientId;

    /**
     * Unique Facility Id representing the {@link Facility} in which this appointment was scheduled
     */
    private String facilityId;

    /**
     * Unique Employee (Doctor) Id for the {@link Employee} who the Patient wishes to see
     * in this appointment
     */
    private String doctorId;

    /**
     * This actually represents the name of the {@link Clinic}
     */
    private String clinicId;

    /**
     * Representing the {@link AppointmentType} name
     * Usually between "New" or "Follow Up"
     */
    private String appointmentTypeId;

    /**
     * Reason for scheduling appointment, as written by the {@link Patient}
     */
    private String appointmentReason;

    /**
     * Flag to check if this Appointment is still active or not
     */
    private Boolean isActive;
    private String age;

    /**
     * The Patient responsible for scheduling this appointment.
     */
    @Ignore
    private Patient patientDetails;

    /**
     * The employee responsible for providing care/attention as regards this Appointment
     */
    @Ignore
    @SerializedName("providerDetails")
    private Employee providerDetails;

    @Ignore
    public Appointment(){

    }

    public Appointment(int id, String facilityName, String _id, String orderStatusId, String category, String startDate, String personId, String patientId, String facilityId, String doctorId, String clinicId, String appointmentTypeId, String appointmentReason, Boolean isActive, String age) {
        this.id = id;
        this.facilityName = facilityName;
        this._id = _id;
        this.orderStatusId = orderStatusId;
        this.category = category;
        this.startDate = startDate;
        this.personId = personId;
        this.patientId = patientId;
        this.facilityId = facilityId;
        this.doctorId = doctorId;
        this.clinicId = clinicId;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentReason = appointmentReason;
        this.isActive = isActive;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = AppUtils.localDateToDbString(startDate);
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(String appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public String getAppointmentReason() {
        return appointmentReason;
    }

    public void setAppointmentReason(String appointmentReason) {
        this.appointmentReason = appointmentReason;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Patient getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(Patient patientDetails) {
        this.patientDetails = patientDetails;
    }

    public Employee getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(Employee providerDetails) {
        this.providerDetails = providerDetails;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "facilityName='" + facilityName + '\'' +
                ", clinicName='" + clinicId + '\'' +
                ", startDate=" + startDate +
                '}';
    }

    @Override
    public int compareTo(@NonNull Appointment o) {
        long date1 = AppUtils.dbStringToLocalDate(getStartDate()).getTime();
        long date2 = AppUtils.dbStringToLocalDate(o.getStartDate()).getTime();

        //descending order
        return Long.compare(date1, date2);
    }
}
