package ng.apmis.apmismobile.data.database.facilityModel;


import java.util.List;

public class Facility {

    private String _id;
    private String website;
    private String shortName;
    private String facilityTypeId;
    private String facilityClassId;
    private String facilityOwnershipId;
    private String street;
    private String city;
    private String state;
    private String country;
    private Address address;
    private String primaryContactPhoneNo;
    private String cacNo;
    private String email;
    private String name;
    private String updatedAt;
    private String createdAt;
    private List<Object> invitees = null;
    private boolean isValidRegistration;
    private boolean status;
    private boolean isTokenVerified;
    private List<MinorLocation> minorLocations = null;
    private List<Object> facilitymoduleId = null;
    private List<Department> departments = null;
    private List<Object> secondaryContactPhoneNo = null;
    private List<Object> memberof = null;
    private List<String> memberFacilities = null;
    private boolean isHostFacility;
    private boolean isNetworkFacility;

    public Facility(String _id, String website, String shortName, String facilityTypeId,
                    String facilityClassId, String facilityOwnershipId, String street, String city,
                    String state, String country, Address address, String primaryContactPhoneNo,
                    String cacNo, String email, String name, String updatedAt, String createdAt,
                    List<Object> invitees, boolean isValidRegistration, boolean status,
                    boolean isTokenVerified, List<MinorLocation> minorLocations,
                    List<Object> facilitymoduleId, List<Department> departments,
                    List<Object> secondaryContactPhoneNo, List<Object> memberof,
                    List<String> memberFacilities, boolean isHostFacility, boolean isNetworkFacility) {
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

    public List<Object> getMemberof() {
        return memberof;
    }

    public void setMemberof(List<Object> memberof) {
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
}
