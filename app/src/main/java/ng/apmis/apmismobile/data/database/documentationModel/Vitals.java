package ng.apmis.apmismobile.data.database.documentationModel;

import android.support.annotation.NonNull;

import ng.apmis.apmismobile.utilities.AppUtils;

public class Vitals implements Comparable<Vitals>{

    private PulseRate pulseRate;
    private Integer respiratoryRate;
    private Double temperature;
    private BodyMass bodyMass;
    private BloodPressure bloodPressure;
    private AbdominalCondition abdominalCondition;
    private String updatedAt;

    public PulseRate getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(PulseRate pulseRate) {
        this.pulseRate = pulseRate;
    }

    public Integer getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(Integer respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public BodyMass getBodyMass() {
        return bodyMass;
    }

    public void setBodyMass(BodyMass bodyMass) {
        this.bodyMass = bodyMass;
    }

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public AbdominalCondition getAbdominalCondition() {
        return abdominalCondition;
    }

    public void setAbdominalCondition(AbdominalCondition abdominalCondition) {
        this.abdominalCondition = abdominalCondition;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Vitals{" +
                "pulseRate=" + pulseRate +
                ", respiratoryRate=" + respiratoryRate +
                ", temperature=" + temperature +
                ", bodyMass=" + bodyMass +
                ", bloodPressure=" + bloodPressure +
                ", abdominalCondition=" + abdominalCondition +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Vitals o) {
        long date1 = AppUtils.dbStringToLocalDate(getUpdatedAt()).getTime();
        long date2 = AppUtils.dbStringToLocalDate(o.getUpdatedAt()).getTime();

        //descending order
        return Long.compare(date1, date2);
    }

    public static class AbdominalCondition {

        private Integer spo2;
        private Integer girth;

        public Integer getSpo2() {
            return spo2;
        }

        public void setSpo2(Integer spo2) {
            this.spo2 = spo2;
        }

        public Integer getGirth() {
            return girth;
        }

        public void setGirth(Integer girth) {
            this.girth = girth;
        }

        @Override
        public String toString() {
            return "AbdominalCondition{" +
                    "spo2=" + spo2 +
                    ", girth=" + girth +
                    '}';
        }

    }

    public static class BloodPressure {

        private Integer systolic;
        private Integer diastolic;
        private String location;
        private String position;

        public Integer getSystolic() {
            return systolic;
        }

        public void setSystolic(Integer systolic) {
            this.systolic = systolic;
        }

        public Integer getDiastolic() {
            return diastolic;
        }

        public void setDiastolic(Integer diastolic) {
            this.diastolic = diastolic;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return "BloodPressure{" +
                    "systolic=" + systolic +
                    ", diastolic=" + diastolic +
                    ", location='" + location + '\'' +
                    ", position='" + position + '\'' +
                    '}';
        }

    }

    public static class BodyMass {

        private Double height;
        private Double weight;
        private Double bmi;

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public Double getBmi() {
            return bmi;
        }

        public void setBmi(Double bmi) {
            this.bmi = bmi;
        }

        @Override
        public String toString() {
            return "BodyMass{" +
                    "height=" + height +
                    ", weight=" + weight +
                    ", bmi=" + bmi +
                    '}';
        }

    }

    public static class PulseRate {

        private Integer pulseRateValue;
        private String location;
        private String rythm;

        public Integer getPulseRateValue() {
            return pulseRateValue;
        }

        public void setPulseRateValue(Integer pulseRateValue) {
            this.pulseRateValue = pulseRateValue;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getRythm() {
            return rythm;
        }

        public void setRythm(String rythm) {
            this.rythm = rythm;
        }

        @Override
        public String toString() {
            return "PulseRate{" +
                    "pulseRateValue=" + pulseRateValue +
                    ", location='" + location + '\'' +
                    ", rythm='" + rythm + '\'' +
                    '}';
        }

    }



}
