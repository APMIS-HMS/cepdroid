package ng.apmis.apmismobile.data.database.facilityModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import ng.apmis.apmismobile.data.database.reviewModel.Review;

public class Address {

    @SerializedName("id")
    private String id;
    private Boolean active;
    private String description;

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions = null;
    private String website;
    private String vicinity;

    @SerializedName("utc_offset")
    private Integer utcOffset;
    private String url;
    private List<String> types = null;
    private String scope;
    private List<Review> reviews = null;
    private String reference;
    private Integer rating;

    @SerializedName("place_id")
    private String placeId;
    private List<Photo> photos = null;

    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    private String name;

    @SerializedName("international_phone_number")
    private String internationalPhoneNumber;
    private String icon;
    private Geometry geometry;

    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;

    @SerializedName("formatted_address")
    private String formattedAddress;

    @SerializedName("adr_address")
    private String adrAddress;

    @SerializedName("address_components")
    private List<AddressComponent> addressComponents = null;
    private String country;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getAdrAddress() {
        return adrAddress;
    }

    public void setAdrAddress(String adrAddress) {
        this.adrAddress = adrAddress;
    }

    public List<AddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<AddressComponent> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    static class AddressComponent {

        private List<String> types = null;

        @SerializedName("short_name")
        private String shortName;

        @SerializedName("long_name")
        private String longName;

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getLongName() {
            return longName;
        }

        public void setLongName(String longName) {
            this.longName = longName;
        }

    }

    static class Photo {

        private Integer width;

        @SerializedName("html_attributions")
        private List<String> htmlAttributions = null;
        private Integer height;

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public List<String> getHtmlAttributions() {
            return htmlAttributions;
        }

        public void setHtmlAttributions(List<String> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

    }

}
