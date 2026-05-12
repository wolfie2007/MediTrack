package com.meditrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Appointment implements Serializable {
    public enum AppointmentStatus {
        SCHEDULED,
        COMPLETED,
        CANCELED
    }

    private final String id;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentAt;
    private String reason;
    private AppointmentStatus status;

    public Appointment(String id, String patientId, String doctorId, LocalDateTime appointmentAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentAt = appointmentAt;
        this.status = AppointmentStatus.SCHEDULED;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getAppointmentAt() {
        return appointmentAt;
    }

    public void setAppointmentAt(LocalDateTime appointmentAt) {
        this.appointmentAt = appointmentAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
