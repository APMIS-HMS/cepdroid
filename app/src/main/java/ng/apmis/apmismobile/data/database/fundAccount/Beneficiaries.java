package ng.apmis.apmismobile.data.database.fundAccount;

/**
 * Created by Thadeus-APMIS on 10/26/2018.
 */

public class Beneficiaries {

    private String name;
    private int image;

    public Beneficiaries () {}

    public Beneficiaries(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Beneficiaries{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
