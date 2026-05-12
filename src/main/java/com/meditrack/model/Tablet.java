package com.meditrack.model;

import java.time.LocalDate;

public class Tablet extends Medicine {
    private String strength;

    public Tablet(String id, String name, String dosage, int stock, LocalDate startDate, LocalDate endDate, String strength) {
        super(id, name, dosage, stock, startDate, endDate);
        this.strength = strength;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    @Override
    public String getType() {
        return "Tablet";
    }
}
