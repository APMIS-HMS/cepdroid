package ng.apmis.apmismobile.data.database.reviewModel;

import com.google.gson.annotations.SerializedName;

public class Review {

    private Integer time;
    private String text;

    @SerializedName("relative_time_description")
    private String relativeTimeDescription;
    private Integer rating;

    @SerializedName("profile_photo_url")
    private String profilePhotoUrl;
    private String language;

    @SerializedName("author_url")
    private String authorUrl;

    @SerializedName("author_name")
    private String authorName;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription) {
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}
