package ng.apmis.apmismobile.data.database;

import io.reactivex.annotations.Nullable;

public class SearchTermItem {

    private String imageURL;
    private String title;
    private String subTitle;

    public SearchTermItem(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public SearchTermItem(@Nullable String imageURL, String title, String subTitle) {
        this.imageURL = imageURL;
        this.title = title;
        this.subTitle = subTitle;
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
