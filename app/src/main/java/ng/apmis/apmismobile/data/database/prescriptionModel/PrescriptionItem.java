package ng.apmis.apmismobile.data.database.prescriptionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrescriptionItem {

    private String _id;

    /**
     * Name of the PrescriptionItem as prescribed by the Prescriber.
     * Not to be mixed up with the brand name of the drug dispensed
     */
    private String genericName;

    /**
     * The ingredients of the prescribed drug
     */
    private List<Object> ingredients = null;

    /**
     * Flag indicating whether or not item was gotten from within
     * the facility or an external source
     */
    private Boolean isExternal;

    /**
     * Flag monitoring if the Prescription item has been dispensed by any Pharmacy
     */
    private Boolean isDispensed;

    /**
     * Contains the information surrounding what was dispensed by the Pharmacy
     * with respect to this PrescriptionItem
     */
    @SerializedName("dispensed")
    @Expose
    private DispensedItem dispensedItem;

    /**
     * Check to see if the current Facility billed this item
     */
    private Boolean isBilled;
    private Boolean initiateBill;

    //TODO Do not save money as double or float
    /**
     * Total cost in Naira
     */
    private Double totalCost;
    private Double cost;

    /**
     * Optional instruction given by the Prescriber as to how thi Prescription item should be taken
     */
    private String patientInstruction;

    /**
     * Date in which Patient should start the prescription item
     */
    private String startDate;

    /**
     * Duration of the dosage period
     */
    private String duration;

    /**
     * Indicates the dosage amount of the prescription item that should be taken
     */
    private String dosage;

    /**
     * The intervals between each dosage
     */
    private String frequency;

    /**
     * Code ID
     */
    private String code;


    /*
        Some Prescription Body Fields
     */

    //The list size of the prescription body
    private int prescriptionListSize;

    //The position of the item in the prescription
    private int positionInPrescriptionList;

    //The prescriber of the drug
    private String prescriberName;

    //The date the prescription item was created
    private String prescriptionDate;




    public List<Object> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Object> ingredients) {
        this.ingredients = ingredients;
    }

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean isExternal) {
        this.isExternal = isExternal;
    }

    public Boolean getIsDispensed() {
        return isDispensed;
    }

    public void setIsDispensed(Boolean isDispensed) {
        this.isDispensed = isDispensed;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public DispensedItem getDispensedItem() {
        return dispensedItem;
    }

    public void setDispensedItem(DispensedItem dispensed) {
        this.dispensedItem = dispensed;
    }

    public Boolean getIsBilled() {
        return isBilled;
    }

    public void setIsBilled(Boolean isBilled) {
        this.isBilled = isBilled;
    }

    public Boolean getInitiateBill() {
        return initiateBill;
    }

    public void setInitiateBill(Boolean initiateBill) {
        this.initiateBill = initiateBill;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getPatientInstruction() {
        return patientInstruction;
    }

    public void setPatientInstruction(String patientInstruction) {
        this.patientInstruction = patientInstruction;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public int getPrescriptionListSize() {
        return prescriptionListSize;
    }

    public void setPrescriptionListSize(int listSize) {
        this.prescriptionListSize = listSize;
    }

    public int getPositionInPrescriptionList() {
        return positionInPrescriptionList;
    }

    public void setPositionInPrescriptionList(int positionInPrescriptionList) {
        this.positionInPrescriptionList = positionInPrescriptionList;
    }

    public String getPrescriberName() {
        return prescriberName;
    }

    public void setPrescriberName(String prescriberName) {
        this.prescriberName = prescriberName;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    /**
     * Model Class carrying information about the Items dispensed for each PrescriptionItem
     */
    private static class DispensedItem {

        private String _id;
        private List<Object> dispensedArray = null;
        private Integer outstandingBalance;
        private Integer totalQtyDispensed;

        public List<Object> getDispensedArray() {
            return dispensedArray;
        }

        public void setDispensedArray(List<Object> dispensedArray) {
            this.dispensedArray = dispensedArray;
        }

        public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        public Integer getOutstandingBalance() {
            return outstandingBalance;
        }

        public void setOutstandingBalance(Integer outstandingBalance) {
            this.outstandingBalance = outstandingBalance;
        }

        public Integer getTotalQtyDispensed() {
            return totalQtyDispensed;
        }

        public void setTotalQtyDispensed(Integer totalQtyDispensed) {
            this.totalQtyDispensed = totalQtyDispensed;
        }

    }

}