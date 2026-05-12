package com.meditrack.ui;

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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PrescriptionManagerController {
    @FXML
    private TableView<PrescriptionRow> prescriptionTable;
    @FXML
    private TableColumn<PrescriptionRow, String> patientColumn;
    @FXML
    private TableColumn<PrescriptionRow, String> medicineColumn;
    @FXML
    private TableColumn<PrescriptionRow, String> statusColumn;
    @FXML
    private TableColumn<PrescriptionRow, String> periodColumn;
    @FXML
    private Label countLabel;
    @FXML
    private ChoiceBox<ChoiceItem> patientChoice;
    @FXML
    private ChoiceBox<ChoiceItem> medicineChoice;
    @FXML
    private TextArea instructionsArea;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private CheckBox activeCheck;

    private final ObservableList<PrescriptionRow> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        medicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));

        prescriptionTable.setItems(rows);
        prescriptionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                fillForm(selected);
            }
        });

        // refresh on repo change
        EventBus.register(() -> Platform.runLater(this::refresh));
        refresh();
    }

    @FXML
    private void savePrescription() {
        ChoiceItem patientItem = patientChoice.getValue();
        ChoiceItem medicineItem = medicineChoice.getValue();
        if (patientItem == null || medicineItem == null) {
            AlertUtil.showError("Missing details", "Please select patient and medicine.");
            return;
        }
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            AlertUtil.showError("Invalid period", "End date cannot be before start date.");
            return;
        }

        DataRepository repository = AppState.getInstance().getRepository();
        PrescriptionRow selected = prescriptionTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            Optional<Prescription> prescription = repository.getPrescriptions().stream()
                    .filter(item -> item.getId().equals(selected.getId()))
                    .findFirst();
            prescription.ifPresent(item -> {
                item.setPatientId(patientItem.id());
                item.setMedicineId(medicineItem.id());
                item.setInstructions(instructionsArea.getText().trim());
                item.setActive(activeCheck.isSelected());
                item.setStartDate(startDate);
                item.setEndDate(endDate);
            });
        } else {
            Prescription prescription = new Prescription(
                    IdGenerator.newId("rx"),
                    patientItem.id(),
                    medicineItem.id()
            );
            prescription.setInstructions(instructionsArea.getText().trim());
            prescription.setActive(activeCheck.isSelected());
            prescription.setStartDate(startDate);
            prescription.setEndDate(endDate);
            repository.getPrescriptions().add(prescription);
        }

        // persist changes and notify other views
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void deletePrescription() {
        PrescriptionRow selected = prescriptionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        DataRepository repository = AppState.getInstance().getRepository();
        String rxId = selected.getId();
        repository.getPrescriptions().removeIf(item -> item.getId().equals(rxId));
        // Remove dose logs for this prescription
        repository.getDoseLogs().removeIf(log -> rxId.equals(log.getPrescriptionId()));
        // Persist changes
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void clearForm() {
        prescriptionTable.getSelectionModel().clearSelection();
        patientChoice.setValue(null);
        medicineChoice.setValue(null);
        instructionsArea.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        activeCheck.setSelected(true);
    }

    private void fillForm(PrescriptionRow row) {
        patientChoice.setValue(findChoice(patientChoice.getItems(), row.getPatientId()));
        medicineChoice.setValue(findChoice(medicineChoice.getItems(), row.getMedicineId()));
        instructionsArea.setText(row.getInstructions());
        startDatePicker.setValue(row.getStartDate());
        endDatePicker.setValue(row.getEndDate());
        activeCheck.setSelected(row.isActive());
    }

    private ChoiceItem findChoice(List<ChoiceItem> items, String id) {
        return items.stream().filter(item -> item.id().equals(id)).findFirst().orElse(null);
    }

    private void refresh() {
        DataRepository repository = AppState.getInstance().getRepository();

        rows.clear();
        for (Prescription prescription : repository.getPrescriptions()) {
            rows.add(PrescriptionRow.fromPrescription(prescription, repository));
        }
        rows.sort(Comparator.comparing(PrescriptionRow::getPatient));
        countLabel.setText(rows.size() + " saved");

        patientChoice.getItems().setAll(buildPatientChoices(repository));
        medicineChoice.getItems().setAll(buildMedicineChoices(repository));
    }

    private List<ChoiceItem> buildPatientChoices(DataRepository repository) {
        return repository.getPatients().stream()
                .map(patient -> new ChoiceItem(patient.getId(), patient.getDisplayLabel()))
                .sorted(Comparator.comparing(ChoiceItem::label))
                .toList();
    }

    private List<ChoiceItem> buildMedicineChoices(DataRepository repository) {
        return repository.getMedicines().stream()
                .map(medicine -> new ChoiceItem(medicine.getId(), medicine.getName()))
                .sorted(Comparator.comparing(ChoiceItem::label))
                .toList();
    }

    public record ChoiceItem(String id, String label) {
        @Override
        public String toString() {
            return label;
        }
    }

    public static class PrescriptionRow {
        private final String id;
        private final String patientId;
        private final String medicineId;
        private final String patient;
        private final String medicine;
        private final String status;
        private final String period;
        private final String instructions;
        private final boolean active;
        private final LocalDate startDate;
        private final LocalDate endDate;

        private PrescriptionRow(String id, String patientId, String medicineId, String patient,
                                String medicine, String status, String period, String instructions, boolean active,
                                LocalDate startDate, LocalDate endDate) {
            this.id = id;
            this.patientId = patientId;
            this.medicineId = medicineId;
            this.patient = patient;
            this.medicine = medicine;
            this.status = status;
            this.period = period;
            this.instructions = instructions;
            this.active = active;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public static PrescriptionRow fromPrescription(Prescription prescription, DataRepository repository) {
            Patient patient = repository.getPatients().stream()
                    .filter(item -> item.getId().equals(prescription.getPatientId()))
                    .findFirst()
                    .orElse(null);
            Medicine medicine = repository.getMedicines().stream()
                    .filter(item -> item.getId().equals(prescription.getMedicineId()))
                    .findFirst()
                    .orElse(null);
            String period = "";
            if (prescription.getStartDate() != null && prescription.getEndDate() != null) {
                period = prescription.getStartDate() + " to " + prescription.getEndDate();
            }
            return new PrescriptionRow(
                    prescription.getId(),
                    prescription.getPatientId(),
                    prescription.getMedicineId(),
                    patient == null ? "" : patient.getDisplayLabel(),
                    medicine == null ? "" : medicine.getName(),
                    prescription.isActive() ? "Active" : "Completed",
                    period,
                    prescription.getInstructions(),
                    prescription.isActive(),
                    prescription.getStartDate(),
                    prescription.getEndDate()
            );
        }

        public String getId() {
            return id;
        }

        public String getPatientId() {
            return patientId;
        }

        public String getMedicineId() {
            return medicineId;
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

        public String getPeriod() {
            return period;
        }

        public String getInstructions() {
            return instructions;
        }

        public boolean isActive() {
            return active;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }
    }
}
