package ng.apmis.apmismobile.data.database.facilityModel;


import java.util.List;

import ng.apmis.apmismobile.annotations.Exclude;

/**
 * Created by mofeejegi-apmis.<br/>
 * Base object representing any organization.
 */
public class Facility {

    private String _id;
    private String updatedAt;
    private String createdAt;

    /**
     * Registered name of the Facility
     */
    private String name;

    /**
     * URL of the Facility's Website
     */
    private String website;

    /**
     * Abbreviated name of the Facility, if available
     */
    private String shortName;

    /**
     * Readable String Id representing the type of facility
     * Usually could be "Hospital"
     */
    private String facilityTypeId;

    /**
     * Readable String Id representing the Class of this facility
     * Usually either "National" or "International"
     */
    private String facilityClassId;

    /**
     * Readable String Id representing the type of ownership
     * in which this Facility is owned
     */
    private String facilityOwnershipId;

    /**
     * Street level address of this Facility
     */
    private String street;

    /**
     * City in which the Facility is Located
     */
    private String city;

    /**
     * State/Province in which this Facility is located
     */
    private String state;

    /**
     * Country in which this Facility is located
     */
    private String country;

    /**
     * {@link Address} component of the Facility
     */
    private Address address;

    /**
     * The major phone number of this Facility
     */
    private String primaryContactPhoneNo;

    /**
     * Working e-mail address of the Facility
     */
    private String email;

    /**
     * Corporate Affairs Commission Registration
     * Number of this Facility or any other legal equivalent
     */
    private String cacNo;

    /**
     * Accessible {@link Department}s in the Facility
     */
    private List<Department> departments = null;

    /**
     * Facility Id(s) in which this Facility is a sub-facility of
     */
    private List<String> memberof = null;

    /**
     * Ids of the sub-facilities of this parent Facility
     */
    private List<String> memberFacilities = null;

    @Exclude
    private String patientIdForPerson;

    private LogoObject logoObject;

    private List<Object> invitees = null;
    private boolean isValidRegistration;
    private boolean status;
    private boolean isTokenVerified;
    private List<MinorLocation> minorLocations = null;
    private List<Object> facilitymoduleId = null;
    private List<Object> secondaryContactPhoneNo = null;
    private boolean isHostFacility;
    private boolean isNetworkFacility;


    public Facility(String _id, String website, String shortName, String facilityTypeId,
                    String facilityClassId, String facilityOwnershipId, String street, String city,
                    String state, String country, Address address, String primaryContactPhoneNo,
                    String cacNo, String email, String name, String updatedAt, String createdAt,
                    List<Object> invitees, boolean isValidRegistration, boolean status,
                    boolean isTokenVerified, List<MinorLocation> minorLocations,
                    List<Object> facilitymoduleId, List<Department> departments,
                    List<Object> secondaryContactPhoneNo, List<String> memberof,
                    List<String> memberFacilities, LogoObject logoObject, boolean isHostFacility, boolean isNetworkFacility) {
        this._id = _id;
        this.website = website;
        this.shortName = shortName;
        this.facilityTypeId = facilityTypeId;
        this.facilityClassId = facilityClassId;
        this.facilityOwnershipId = facilityOwnershipId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.country = country;
        this.address = address;
        this.primaryContactPhoneNo = primaryContactPhoneNo;
        this.cacNo = cacNo;
        this.email = email;
        this.name = name;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.invitees = invitees;
        this.isValidRegistration = isValidRegistration;
        this.status = status;
        this.isTokenVerified = isTokenVerified;
        this.minorLocations = minorLocations;
        this.facilitymoduleId = facilitymoduleId;
        this.departments = departments;
        this.secondaryContactPhoneNo = secondaryContactPhoneNo;
        this.memberof = memberof;
        this.memberFacilities = memberFacilities;
        this.logoObject = logoObject;
        this.isHostFacility = isHostFacility;
        this.isNetworkFacility = isNetworkFacility;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public String getFacilityClassId() {
        return facilityClassId;
    }

    public void setFacilityClassId(String facilityClassId) {
        this.facilityClassId = facilityClassId;
    }

    public String getFacilityOwnershipId() {
        return facilityOwnershipId;
    }

    public void setFacilityOwnershipId(String facilityOwnershipId) {
        this.facilityOwnershipId = facilityOwnershipId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPrimaryContactPhoneNo() {
        return primaryContactPhoneNo;
    }

    public void setPrimaryContactPhoneNo(String primaryContactPhoneNo) {
        this.primaryContactPhoneNo = primaryContactPhoneNo;
    }

    public String getCacNo() {
        return cacNo;
    }

    public void setCacNo(String cacNo) {
        this.cacNo = cacNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Object> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<Object> invitees) {
        this.invitees = invitees;
    }

    public boolean getIsValidRegistration() {
        return isValidRegistration;
    }

    public void setIsValidRegistration(boolean isValidRegistration) {
        this.isValidRegistration = isValidRegistration;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getIsTokenVerified() {
        return isTokenVerified;
    }

    public void setIsTokenVerified(boolean isTokenVerified) {
        this.isTokenVerified = isTokenVerified;
    }

    public List<MinorLocation> getMinorLocations() {
        return minorLocations;
    }

    public void setMinorLocations(List<MinorLocation> minorLocations) {
        this.minorLocations = minorLocations;
    }

    public List<Object> getFacilitymoduleId() {
        return facilitymoduleId;
    }

    public void setFacilitymoduleId(List<Object> facilitymoduleId) {
        this.facilitymoduleId = facilitymoduleId;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Object> getSecondaryContactPhoneNo() {
        return secondaryContactPhoneNo;
    }

    public void setSecondaryContactPhoneNo(List<Object> secondaryContactPhoneNo) {
        this.secondaryContactPhoneNo = secondaryContactPhoneNo;
    }

    public List<String> getMemberof() {
        return memberof;
    }

    public void setMemberof(List<String> memberof) {
        this.memberof = memberof;
    }

    public List<String> getMemberFacilities() {
        return memberFacilities;
    }

    public void setMemberFacilities(List<String> memberFacilities) {
        this.memberFacilities = memberFacilities;
    }

    public boolean getIsHostFacility() {
        return isHostFacility;
    }

    public void setIsHostFacility(boolean isHostFacility) {
        this.isHostFacility = isHostFacility;
    }

    public boolean getIsNetworkFacility() {
        return isNetworkFacility;
    }

    public void setIsNetworkFacility(boolean isNetworkFacility) {
        this.isNetworkFacility = isNetworkFacility;
    }

    public LogoObject getLogoObject() {
        return logoObject;
    }

    public void setLogoObject(LogoObject logoObject) {
        this.logoObject = logoObject;
    }

    @Override
    public String toString() {
        return "Facility{" +
                "_id='" + _id + '\'' +
                ", website='" + website + '\'' +
                ", shortName='" + shortName + '\'' +
                ", facilityTypeId='" + facilityTypeId + '\'' +
                ", facilityClassId='" + facilityClassId + '\'' +
                ", facilityOwnershipId='" + facilityOwnershipId + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", address=" + address +
                ", primaryContactPhoneNo='" + primaryContactPhoneNo + '\'' +
                ", cacNo='" + cacNo + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", invitees=" + invitees +
                ", isValidRegistration=" + isValidRegistration +
                ", status=" + status +
                ", isTokenVerified=" + isTokenVerified +
                ", minorLocations=" + minorLocations +
                ", facilitymoduleId=" + facilitymoduleId +
                ", departments=" + departments +
                ", secondaryContactPhoneNo=" + secondaryContactPhoneNo +
                ", memberof=" + memberof +
                ", memberFacilities=" + memberFacilities +
                ", isHostFacility=" + isHostFacility +
                ", isNetworkFacility=" + isNetworkFacility +
                '}';
    }

    public String getPatientIdForPerson() {
        return patientIdForPerson;
    }

    public void setPatientIdForPerson(String patientIdForPerson) {
        this.patientIdForPerson = patientIdForPerson;
    }
}
