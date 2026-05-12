package com.meditrack.model;

import java.time.LocalDate;

public class Injection extends Medicine {
    private String site;

    public Injection(String id, String name, String dosage, int stock, LocalDate startDate, LocalDate endDate, String site) {
        super(id, name, dosage, stock, startDate, endDate);
        this.site = site;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String getType() {
        return "Injection";
    }
}
