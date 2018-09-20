package ng.apmis.apmismobile.data.database.documentationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ng.apmis.apmismobile.annotations.Exclude;

public class Document implements Serializable {

    private DocumentType documentType;

    @Exclude
    private String body;

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public JSONObject getBody() throws JSONException {
        return new JSONObject(body);
    }

    public void setBody(JSONObject body) {
        this.body = body.toString();
    }

}
