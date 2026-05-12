package com.meditrack.model;

import java.time.LocalDate;

public class Syrup extends Medicine {
    private String flavor;

    public Syrup(String id, String name, String dosage, int stock, LocalDate startDate, LocalDate endDate, String flavor) {
        super(id, name, dosage, stock, startDate, endDate);
        this.flavor = flavor;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    @Override
    public String getType() {
        return "Syrup";
    }
}
