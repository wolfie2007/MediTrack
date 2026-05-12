package com.meditrack.model;

import java.time.LocalDate;

public class Patient extends Person {
    private LocalDate dateOfBirth;
    private String notes;

    public Patient(String id, String fullName, String phone, String email, LocalDate dateOfBirth) {
        super(id, fullName, phone, email);
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String getDisplayLabel() {
        return getFullName();
    }
}
