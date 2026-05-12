package com.meditrack.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Medicine implements Serializable {
    private final String id;
    private String name;
    private String dosage;
    private int stock;
    private LocalDate startDate;
    private LocalDate endDate;

    protected Medicine(String id, String name, String dosage, int stock, LocalDate startDate, LocalDate endDate) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = name;
        this.dosage = dosage;
        this.stock = stock;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public abstract String getType();
}
