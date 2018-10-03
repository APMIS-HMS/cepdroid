package ng.apmis.apmismobile.data.database.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ng.apmis.apmismobile.annotations.Exclude;

/**
 * Person model has all details about the person (Patient)
 * Class is also a {@link Room} database entity
 */

@Entity(tableName = "person", indices = {@Index(value = "apmisId", unique = true)})
public class PersonEntry {

    /**
     * Id for saving in Room Database
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * APMIS Id of the Person received upon online registration
     */
    private String apmisId;

    /**
     * Person's title "Mr, Mrs, Miss, etc"
     */
    private String title;

    /**
     * Person's first name
     */
    private String firstName;

    /**
     * Person's last name
     */
    private String lastName;

    /**
     * There are only two genders, yes. Male and Female
     */
    private String gender;

    /**
     * Mother's maiden name, used for security purposes
     */
    private String motherMaidenName;

    /**
     * Security Question chosen for extra verification
     */
    private String securityQuestion;

    /**
     * Answer to Security question
     */
    private String securityAnswer;

    /**
     *
     */
    private String primaryContactPhoneNo;
    @Exclude
    private String secondaryContactPhoneNo;
    private String dateOfBirth;
    private String email;
    private String otherNames;
    private String biometric; //Buffer according to web
    @Exclude
    private String personProfessions; //[personProfessionsSchema]
    private String nationality;
    private String stateOfOrigin;
    private String lgaOfOrigin;
    private String profileImageObject; //Store Image uri
    @Exclude
    private String homeAddress;
    private String maritalStatus;
    @Exclude
    private String nextOfKin;
    @Exclude
    private String wallet;

    @Ignore
    public PersonEntry(String apmisId, String title, String firstName, String lastName, String gender, String motherMaidenName, String securityQuestion, String securityAnswer, String primaryContactPhoneNo, String secondaryContactPhoneNo, String dateOfBirth, String email, String otherNames, String biometric, String nationality, String stateOfOrigin, String lgaOfOrigin, String profileImageObject, String homeAddress, String maritalStatus, String nextOfKin) {
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
        this.nationality = nationality;
        this.stateOfOrigin = stateOfOrigin;
        this.lgaOfOrigin = lgaOfOrigin;
        this.profileImageObject = profileImageObject;
        this.homeAddress = homeAddress;
        this.maritalStatus = maritalStatus;
        this.nextOfKin = nextOfKin;
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
