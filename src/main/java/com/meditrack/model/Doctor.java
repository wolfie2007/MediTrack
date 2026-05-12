package com.meditrack.model;

public class Doctor extends Person {
    private String specialization;
    private String clinic;

    public Doctor(String id, String fullName, String phone, String email, String specialization, String clinic) {
        super(id, fullName, phone, email);
        this.specialization = specialization;
        this.clinic = clinic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    @Override
    public String getDisplayLabel() {
        return getFullName() + " - " + specialization;
    }
}
