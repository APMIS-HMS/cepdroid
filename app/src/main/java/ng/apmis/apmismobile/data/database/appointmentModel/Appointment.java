package ng.apmis.apmismobile.data.database.appointmentModel;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import ng.apmis.apmismobile.data.database.patientModel.Patient;
import ng.apmis.apmismobile.utilities.AppUtils;

/**
 * Created by Thadeus-APMIS on 6/11/2018.
 */

@Entity(tableName = "appointments", indices = {@Index(value = {"_id"}, unique = true)})
public class Appointment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String facilityName;

    @Ignore
    private String clinicName;
    @Ignore
    private long appointmentDateTime;

    private String _id;
    private String orderStatusId;
    private String category;
    private String startDate;
    private String personId;
    private String patientId;
    private String facilityId;
    private String doctorId;
    private String clinicId;
    private String appointmentTypeId;
    private String appointmentReason;
    @Ignore
    private String createdAt;
    @Ignore
    private String updatedAt;
    private Boolean isActive;
    private String age;

    @Ignore
    private Patient patientDetails;

    @Ignore
    public Appointment(){

    }

    @Ignore
    public Appointment(String facilityName, String clinicName, long appointmentDateTime) {
        this.facilityName = facilityName;
        this.clinicName = clinicName;
        this.appointmentDateTime = appointmentDateTime;
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

    @Override
    public String toString() {
        return "Appointments{" +
                "facilityName='" + facilityName + '\'' +
                ", clinicName='" + clinicId + '\'' +
                ", startDate=" + startDate +
                '}';
    }
}
