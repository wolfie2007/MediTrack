package com.meditrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class DoseLog implements Serializable {
    public enum DoseStatus {
        TAKEN,
        MISSED,
        SKIPPED
    }

    private final String id;
    private String prescriptionId;
    private LocalDateTime scheduledAt;
    private LocalDateTime takenAt;
    private DoseStatus status;
    private String notes;

    public DoseLog(String id, String prescriptionId, LocalDateTime scheduledAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.prescriptionId = prescriptionId;
        this.scheduledAt = scheduledAt;
        this.status = DoseStatus.MISSED;
    }

    public String getId() {
        return id;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }

    public DoseStatus getStatus() {
        return status;
    }

    public void setStatus(DoseStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
