package ng.apmis.apmismobile.data.database.documentationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DocumentType implements Serializable{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("typeOfDocumentId")
    @Expose
    private String typeOfDocumentId;
    @SerializedName("scopeLevelId")
    @Expose
    private String scopeLevelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("facilityId")
    @Expose
    private String facilityId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("isSide")
    @Expose
    private Boolean isSide;
    @SerializedName("moduleIds")
    @Expose
    private List<Object> moduleIds = null;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeOfDocumentId() {
        return typeOfDocumentId;
    }

    public void setTypeOfDocumentId(String typeOfDocumentId) {
        this.typeOfDocumentId = typeOfDocumentId;
    }

    public String getScopeLevelId() {
        return scopeLevelId;
    }

    public void setScopeLevelId(String scopeLevelId) {
        this.scopeLevelId = scopeLevelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
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

    public Boolean getIsSide() {
        return isSide;
    }

    public void setIsSide(Boolean isSide) {
        this.isSide = isSide;
    }

    public List<Object> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<Object> moduleIds) {
        this.moduleIds = moduleIds;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
