package ng.apmis.apmismobile.data.database.fundAccount;

public class Price {

    private String name;

    private Boolean isBase;

    private String priceId;

    private Integer price;

    private String _id;

    private String modifierType;

    private Integer modifierValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsBase() {
        return isBase;
    }

    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getModifierType() {
        return modifierType;
    }

    public void setModifierType(String modifierType) {
        this.modifierType = modifierType;
    }

    public Integer getModifierValue() {
        return modifierValue;
    }

    public void setModifierValue(Integer modifierValue) {
        this.modifierValue = modifierValue;
    }

    @Override
    public String toString() {
        return name + " (₦" +price + ")";
    }
}
