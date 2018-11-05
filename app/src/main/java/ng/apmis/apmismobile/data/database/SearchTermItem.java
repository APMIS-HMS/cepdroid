package ng.apmis.apmismobile.data.database;

import io.reactivex.annotations.Nullable;

public class SearchTermItem {

    private String id;
    private String imageURL;
    private String title;
    private String subTitle;
    private String type;


    public SearchTermItem(String id, String title, String subTitle, String type) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.type = type;
    }

    public SearchTermItem(@Nullable String imageURL, String id, String title, String subTitle, String type) {
        this.id = id;
        this.imageURL = imageURL;
        this.title = title;
        this.subTitle = subTitle;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public String toString() {
        return "SearchTermItem{" +
                "title=" + title +
                ", subTitle='" + subTitle + '\'' +
                '}';
    }
}
