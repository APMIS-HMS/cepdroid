package ng.apmis.apmismobile.data.database.documentationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ng.apmis.apmismobile.annotations.Exclude;

/**
 * Created by mofeejegi-apmis.
 * The <code>Document</code> object is a property of the {@link Documentation}
 * class. It contains the Body object which contains doctor's reports or vitals of
 * any {@link Documentation}.
 */
public class Document implements Serializable {

    private DocumentType documentType;

    //Exclude the body item, would be parsed to a JSONObject when received from server
    @Exclude
    private String body;

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    /**
     * Body item, containing doctor's reports or vitals.
     * @return The body item as a JSONObject
     * @throws JSONException e
     */
    public JSONObject getBody() throws JSONException {
        return new JSONObject(body);
    }

    /**
     * Sets the body item, by passing in a JSONObject containing Doctor's reports
     * It is further saved as a String
     */
    public void setBody(JSONObject body) {
        this.body = body.toString();
    }

}
