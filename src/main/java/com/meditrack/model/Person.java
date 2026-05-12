package com.meditrack.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class Person implements Serializable {
    private final String id;
    private String fullName;
    private String phone;
    private String email;

    protected Person(String id, String fullName, String phone, String email) {
        this.id = Objects.requireNonNull(id, "id");
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract String getDisplayLabel();
}
