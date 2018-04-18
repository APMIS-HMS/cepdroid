package ng.apmis.apmismobile.data.database.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Date;

/**
 * Person model has all details about the person (Patient)
 */

@Entity(tableName = "person", indices = {@Index(value = "apmisId", unique = true)})
public class PersonEntry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String apmisId;
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String motherMaidenName;
    private String securityQuestion;
    private String securityAnswer;
    private String primaryContactPhoneNo;
    @Expose
    private String secondaryContactPhoneNo;
    private String dateOfBirth;
    private String email;
    private String otherNames;
    private String biometric; //Buffer according to web
    @Expose
    private String personProfessions; //[personProfessionsSchema]
    private String nationality;
    private String stateOfOrigin;
    private String lgaOfOrigin;
    private String profileImageObject; //Store Image uri
    private String homeAddress;
    private String maritalStatus;
    @Expose
    private String nextOfKin;
    @Expose
    private String wallet;

    @Ignore
    public PersonEntry(String apmisId, String title, String firstName, String lastName, String gender, String motherMaidenName, String securityQuestion, String securityAnswer, String primaryContactPhoneNo, String secondaryContactPhoneNo, String dateOfBirth, String email, String otherNames, String biometric, String personProfessions, String nationality, String stateOfOrigin, String lgaOfOrigin, String profileImageObject, String homeAddress, String maritalStatus, String nextOfKin, String wallet) {
        this.apmisId = apmisId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.motherMaidenName = motherMaidenName;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.secondaryContactPhoneNo = new Gson().toJson(secondaryContactPhoneNo);
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.otherNames = otherNames;
        this.biometric = biometric;
        this.personProfessions = personProfessions;
        this.nationality = nationality;
        this.stateOfOrigin = stateOfOrigin;
        this.lgaOfOrigin = lgaOfOrigin;
        this.profileImageObject = profileImageObject;
        this.homeAddress = homeAddress;
        this.maritalStatus = maritalStatus;
        this.nextOfKin = nextOfKin;
        this.wallet = wallet;
    }

    public PersonEntry(int id, String apmisId, String title, String firstName, String lastName, String gender, String motherMaidenName, String securityQuestion, String securityAnswer, String primaryContactPhoneNo, String secondaryContactPhoneNo, String dateOfBirth, String email, String otherNames, String biometric, String personProfessions, String nationality, String stateOfOrigin, String lgaOfOrigin, String profileImageObject, String homeAddress, String maritalStatus, String nextOfKin, String wallet) {
        this.id = id;
        this.apmisId = apmisId;
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.motherMaidenName = motherMaidenName;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.secondaryContactPhoneNo = secondaryContactPhoneNo;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.otherNames = otherNames;
        this.biometric = biometric;
        this.personProfessions = personProfessions;
        this.nationality = nationality;
        this.stateOfOrigin = stateOfOrigin;
        this.lgaOfOrigin = lgaOfOrigin;
        this.profileImageObject = profileImageObject;
        this.homeAddress = homeAddress;
        this.maritalStatus = maritalStatus;
        this.nextOfKin = nextOfKin;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApmisId() {
        return apmisId;
    }

    public void setApmisId(String apmisId) {
        this.apmisId = apmisId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public String getPrimaryContactPhoneNo() {
        return primaryContactPhoneNo;
    }

    public void setPrimaryContactPhoneNo(String primaryContactPhoneNo) {
        this.primaryContactPhoneNo = primaryContactPhoneNo;
    }

    public String getSecondaryContactPhoneNo() {
        return secondaryContactPhoneNo;
    }

    public void setSecondaryContactPhoneNo(String secondaryContactPhoneNo) {
        this.secondaryContactPhoneNo = secondaryContactPhoneNo;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getBiometric() {
        return biometric;
    }

    public void setBiometric(String biometric) {
        this.biometric = biometric;
    }

    public String getPersonProfessions() {
        return personProfessions;
    }

    public void setPersonProfessions(String personProfessions) {
        this.personProfessions = personProfessions;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getStateOfOrigin() {
        return stateOfOrigin;
    }

    public void setStateOfOrigin(String stateOfOrigin) {
        this.stateOfOrigin = stateOfOrigin;
    }

    public String getLgaOfOrigin() {
        return lgaOfOrigin;
    }

    public void setLgaOfOrigin(String lgaOfOrigin) {
        this.lgaOfOrigin = lgaOfOrigin;
    }

    public String getProfileImageObject() {
        return profileImageObject;
    }

    public void setProfileImageObject(String profileImageObject) {
        this.profileImageObject = profileImageObject;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "PersonEntry{" +
                "id=" + id +
                ", apmisId='" + apmisId + '\'' +
                ", title='" + title + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", motherMaidenName='" + motherMaidenName + '\'' +
                ", securityQuestion='" + securityQuestion + '\'' +
                ", securityAnswer='" + securityAnswer + '\'' +
                ", primaryContactPhoneNo='" + primaryContactPhoneNo + '\'' +
                ", secondaryContactPhoneNo=" + secondaryContactPhoneNo + '\'' +
                ", dateOfBirth=" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", otherNames='" + otherNames + '\'' +
                ", biometric='" + biometric + '\'' +
                ", personProfessions=" + personProfessions + '\'' +
                ", nationality='" + nationality + '\'' +
                ", stateOfOrigin='" + stateOfOrigin + '\'' +
                ", lgaOfOrigin='" + lgaOfOrigin + '\'' +
                ", profileImageObject='" + profileImageObject + '\'' +
                ", homeAddress='" + homeAddress + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", nextOfKin=" + nextOfKin + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
