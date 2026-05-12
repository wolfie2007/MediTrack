package com.meditrack.model;

import java.time.LocalDate;

public class FamilyMember extends Patient {
    private String relationship;

    public FamilyMember(String id, String fullName, String phone, String email, LocalDate dateOfBirth, String relationship) {
        super(id, fullName, phone, email, dateOfBirth);
        this.relationship = relationship;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String getDisplayLabel() {
        return getFullName() + " (" + relationship + ")";
    }
}
