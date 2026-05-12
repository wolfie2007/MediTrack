package com.meditrack.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Prescription implements Serializable {
    private final String id;
    private String patientId;
    private String medicineId;
    private String instructions;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;

    public Prescription(String id, String patientId, String medicineId) {
        this.id = Objects.requireNonNull(id, "id");
        this.patientId = patientId;
        this.medicineId = medicineId;
        this.active = true;
    }

    public String getId() {
        return id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
