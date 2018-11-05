package ng.apmis.apmismobile.data.database.facilityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogoObject {

    @SerializedName("encoding")
    @Expose
    private String encoding;
    @SerializedName("mimetype")
    @Expose
    private String mimetype;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("detailthumbnail")
    @Expose
    private String detailthumbnail;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDetailthumbnail() {
        return detailthumbnail;
    }

    public void setDetailthumbnail(String detailthumbnail) {
        this.detailthumbnail = detailthumbnail;
    }

}