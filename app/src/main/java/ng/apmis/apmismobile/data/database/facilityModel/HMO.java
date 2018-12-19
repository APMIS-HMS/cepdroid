package ng.apmis.apmismobile.data.database.facilityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HMO {

    @SerializedName("hmo")
    @Expose
    private String hmo;
    @SerializedName("hmoName")
    @Expose
    private String hmoName;

    public String getHmo() {
        return hmo;
    }

    public void setHmo(String hmo) {
        this.hmo = hmo;
    }

    public String getHmoName() {
        return hmoName;
    }

    public void setHmoName(String hmoName) {
        this.hmoName = hmoName;
    }

    @Override
    public String toString() {
        return hmoName;
    }
}
