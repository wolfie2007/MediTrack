package com.meditrack.ui;

import com.meditrack.model.DoseLog;
import com.meditrack.model.Medicine;
import com.meditrack.model.Patient;
import com.meditrack.model.Prescription;
import com.meditrack.storage.AppState;
import com.meditrack.storage.DataRepository;
import com.meditrack.util.EventBus;
import javafx.application.Platform;
import com.meditrack.util.AlertUtil;
import com.meditrack.util.IdGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class DoseTrackerController {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    private TableView<DoseRow> doseTable;
    @FXML
    private TableColumn<DoseRow, String> timeColumn;
    @FXML
    private TableColumn<DoseRow, String> patientColumn;
    @FXML
    private TableColumn<DoseRow, String> medicineColumn;
    @FXML
    private TableColumn<DoseRow, String> statusColumn;
    @FXML
    private TableColumn<DoseRow, String> notesColumn;
    @FXML
    private Label countLabel;
    @FXML
    private ChoiceBox<PrescriptionChoice> prescriptionChoice;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private ChoiceBox<DoseLog.DoseStatus> statusChoice;
    @FXML
    private TextArea notesArea;

    private final ObservableList<DoseRow> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        medicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));

        statusChoice.getItems().setAll(DoseLog.DoseStatus.values());
        statusChoice.setValue(DoseLog.DoseStatus.MISSED);
        datePicker.setValue(LocalDate.now());

        doseTable.setItems(rows);
        doseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                fillForm(selected);
            }
        });

        // update when repository changes
        EventBus.register(() -> Platform.runLater(this::refresh));
        refresh();
    }

    @FXML
    private void saveDose() {
        PrescriptionChoice selectedPrescription = prescriptionChoice.getValue();
        if (selectedPrescription == null) {
            AlertUtil.showError("Missing prescription", "Please select a prescription.");
            return;
        }
        LocalDate date = datePicker.getValue();
        LocalTime time = parseTime(timeField.getText());
        if (date == null) {
            AlertUtil.showError("Missing date", "Please select a date.");
            return;
        }
        if (time == null) {
            AlertUtil.showError("Invalid time", "Time must be in HH:mm format.");
            return;
        }
        LocalDateTime scheduledAt = date == null || time == null ? null : LocalDateTime.of(date, time);

        DataRepository repository = AppState.getInstance().getRepository();
        DoseRow selectedRow = doseTable.getSelectionModel().getSelectedItem();

        if (selectedRow != null) {
            Optional<DoseLog> log = repository.getDoseLogs().stream()
                    .filter(item -> item.getId().equals(selectedRow.getId()))
                    .findFirst();
            log.ifPresent(item -> {
                item.setPrescriptionId(selectedPrescription.id());
                item.setScheduledAt(scheduledAt);
                item.setStatus(statusChoice.getValue());
                item.setNotes(notesArea.getText().trim());
            });
        } else {
            DoseLog log = new DoseLog(IdGenerator.newId("dose"), selectedPrescription.id(), scheduledAt);
            log.setStatus(statusChoice.getValue());
            log.setNotes(notesArea.getText().trim());
            repository.getDoseLogs().add(log);
        }

        // persist and notify other views
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void deleteDose() {
        DoseRow selectedRow = doseTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            return;
        }
        DataRepository repository = AppState.getInstance().getRepository();
        repository.getDoseLogs().removeIf(item -> item.getId().equals(selectedRow.getId()));
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void markTaken() {
        updateStatus(DoseLog.DoseStatus.TAKEN, LocalDateTime.now());
    }

    @FXML
    private void markMissed() {
        updateStatus(DoseLog.DoseStatus.MISSED, null);
    }

    @FXML
    private void clearForm() {
        doseTable.getSelectionModel().clearSelection();
        prescriptionChoice.setValue(null);
        datePicker.setValue(LocalDate.now());
        timeField.clear();
        statusChoice.setValue(DoseLog.DoseStatus.MISSED);
        notesArea.clear();
    }

    private void updateStatus(DoseLog.DoseStatus status, LocalDateTime takenAt) {
        DoseRow selectedRow = doseTable.getSelectionModel().getSelectedItem();
        if (selectedRow == null) {
            return;
        }
        DataRepository repository = AppState.getInstance().getRepository();
        repository.getDoseLogs().stream()
                .filter(item -> item.getId().equals(selectedRow.getId()))
                .findFirst()
                .ifPresent(item -> {
                    item.setStatus(status);
                    item.setTakenAt(takenAt);
                });
        repository.saveAll();
        refresh();
    }

    private void fillForm(DoseRow row) {
        prescriptionChoice.setValue(findPrescriptionChoice(row.getPrescriptionId()));
        datePicker.setValue(row.getDate());
        timeField.setText(row.getTimeRaw());
        statusChoice.setValue(row.getStatusValue());
        notesArea.setText(row.getNotes());
    }

    private PrescriptionChoice findPrescriptionChoice(String id) {
        return prescriptionChoice.getItems().stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    private LocalTime parseTime(String raw) {
        try {
            return LocalTime.parse(raw, TIME_FORMAT);
        } catch (Exception ex) {
            return null;
        }
    }

    private void refresh() {
        DataRepository repository = AppState.getInstance().getRepository();
        prescriptionChoice.getItems().setAll(repository.getPrescriptions().stream()
                .map(rx -> new PrescriptionChoice(rx.getId(), labelFor(rx, repository)))
                .sorted(Comparator.comparing(PrescriptionChoice::label))
                .toList());

        rows.clear();
        for (DoseLog log : repository.getDoseLogs()) {
            rows.add(DoseRow.fromLog(log, repository));
        }
        rows.sort(Comparator.comparing(DoseRow::getTimeRaw, String::compareToIgnoreCase));
        countLabel.setText(rows.size() + " saved");
    }

    private String labelFor(Prescription prescription, DataRepository repository) {
        Patient patient = repository.getPatients().stream()
                .filter(item -> item.getId().equals(prescription.getPatientId()))
                .findFirst()
                .orElse(null);
        Medicine medicine = repository.getMedicines().stream()
                .filter(item -> item.getId().equals(prescription.getMedicineId()))
                .findFirst()
                .orElse(null);
        String patientLabel = patient == null ? "" : patient.getDisplayLabel();
        String medicineLabel = medicine == null ? "" : medicine.getName();
        return patientLabel + " - " + medicineLabel;
    }

    public record PrescriptionChoice(String id, String label) {
        @Override
        public String toString() {
            return label;
        }
    }

    public static class DoseRow {
        private final String id;
        private final String prescriptionId;
        private final LocalDate date;
        private final String timeRaw;
        private final String patient;
        private final String medicine;
        private final String status;
        private final String notes;
        private final DoseLog.DoseStatus statusValue;

        private DoseRow(String id, String prescriptionId, LocalDate date, String timeRaw, String patient, String medicine,
                        String status, String notes, DoseLog.DoseStatus statusValue) {
            this.id = id;
            this.prescriptionId = prescriptionId;
            this.date = date;
            this.timeRaw = timeRaw;
            this.patient = patient;
            this.medicine = medicine;
            this.status = status;
            this.notes = notes;
            this.statusValue = statusValue;
        }

        public static DoseRow fromLog(DoseLog log, DataRepository repository) {
            Prescription prescription = repository.getPrescriptions().stream()
                    .filter(item -> item.getId().equals(log.getPrescriptionId()))
                    .findFirst()
                    .orElse(null);
            Patient patient = prescription == null ? null : repository.getPatients().stream()
                    .filter(item -> item.getId().equals(prescription.getPatientId()))
                    .findFirst()
                    .orElse(null);
            Medicine medicine = prescription == null ? null : repository.getMedicines().stream()
                    .filter(item -> item.getId().equals(prescription.getMedicineId()))
                    .findFirst()
                    .orElse(null);
            LocalDateTime scheduledAt = log.getScheduledAt();
            String time = scheduledAt == null ? "" : scheduledAt.toLocalTime().format(TIME_FORMAT);
            LocalDate date = scheduledAt == null ? null : scheduledAt.toLocalDate();

            return new DoseRow(
                    log.getId(),
                    log.getPrescriptionId(),
                    date,
                    time,
                    patient == null ? "" : patient.getDisplayLabel(),
                    medicine == null ? "" : medicine.getName(),
                    log.getStatus().name(),
                    log.getNotes() == null ? "" : log.getNotes(),
                    log.getStatus()
            );
        }

        public String getId() {
            return id;
        }

        public String getPrescriptionId() {
            return prescriptionId;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getTimeRaw() {
            return timeRaw;
        }

        public String getTime() {
            if (date == null || timeRaw.isEmpty()) {
                return "";
            }
            return date + " " + timeRaw;
        }

        public String getPatient() {
            return patient;
        }

        public String getMedicine() {
            return medicine;
        }

        public String getStatus() {
            return status;
        }

        public String getNotes() {
            return notes;
        }

        public DoseLog.DoseStatus getStatusValue() {
            return statusValue;
        }
    }
}
