package com.meditrack.ui;

import com.meditrack.model.Patient;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PatientProfilesController {
    @FXML
    private TableView<ProfileRow> profilesTable;
    @FXML
    private TableColumn<ProfileRow, String> typeColumn;
    @FXML
    private TableColumn<ProfileRow, String> nameColumn;
    @FXML
    private TableColumn<ProfileRow, String> dobColumn;
    @FXML
    private TableColumn<ProfileRow, String> phoneColumn;
    @FXML
    private Label countLabel;
    @FXML
    private ChoiceBox<String> typeChoice;
    @FXML
    private TextField nameField;
    @FXML
    private DatePicker dobPicker;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;

    private final ObservableList<ProfileRow> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        typeChoice.getItems().setAll("Patient");
        typeChoice.setValue("Patient");

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        profilesTable.setItems(rows);
        profilesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                fillForm(selected);
                typeChoice.setDisable(true);
            }
        });

        // refresh on repository changes
        EventBus.register(() -> Platform.runLater(this::refresh));
        refresh();
    }

    @FXML
    private void saveProfile() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            AlertUtil.showError("Missing name", "Please enter a full name.");
            return;
        }
        LocalDate dob = dobPicker.getValue();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        DataRepository repository = AppState.getInstance().getRepository();
        ProfileRow selected = profilesTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            updateExisting(selected, name, dob, phone, email);
        } else {
            Patient patient = new Patient(
                    IdGenerator.newId("pat"),
                    name,
                    phone,
                    email,
                    dob
            );
            repository.getPatients().add(patient);
        }

        // persist and notify other views
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void deleteProfile() {
        ProfileRow selected = profilesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        DataRepository repository = AppState.getInstance().getRepository();
        String patientId = selected.getId();

        // Find and remove prescriptions for this patient
        Set<String> removedPrescriptionIds = repository.getPrescriptions().stream()
                .filter(rx -> patientId.equals(rx.getPatientId()))
                .map(rx -> rx.getId())
                .collect(Collectors.toSet());
        repository.getPrescriptions().removeIf(rx -> patientId.equals(rx.getPatientId()));

        // Remove dose logs associated with removed prescriptions
        if (!removedPrescriptionIds.isEmpty()) {
            repository.getDoseLogs().removeIf(log -> removedPrescriptionIds.contains(log.getPrescriptionId()));
        }

        // Finally remove the patient
        repository.getPatients().removeIf(patient -> patient.getId().equals(patientId));

        // Persist all changes (patients, prescriptions, dose logs, appointments)
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void clearForm() {
        profilesTable.getSelectionModel().clearSelection();
        typeChoice.setValue("Patient");
        nameField.clear();
        dobPicker.setValue(null);
        phoneField.clear();
        emailField.clear();
        typeChoice.setDisable(false);
    }

    private void updateExisting(ProfileRow selected, String name, LocalDate dob, String phone, String email) {
        DataRepository repository = AppState.getInstance().getRepository();
        Optional<Patient> patient = repository.getPatients().stream()
                .filter(item -> item.getId().equals(selected.getId()))
                .findFirst();
        patient.ifPresent(item -> {
            item.setFullName(name);
            item.setDateOfBirth(dob);
            item.setPhone(phone);
            item.setEmail(email);
        });
    }

    private void fillForm(ProfileRow row) {
        typeChoice.setValue(row.getType());
        nameField.setText(row.getName());
        dobPicker.setValue(row.getDobValue());
        phoneField.setText(row.getPhone());
        emailField.setText(row.getEmail());
    }

    private void refresh() {
        DataRepository repository = AppState.getInstance().getRepository();
        rows.clear();

        for (Patient patient : repository.getPatients()) {
            rows.add(ProfileRow.fromPatient(patient));
        }

        rows.sort(Comparator.comparing(ProfileRow::getName));
        countLabel.setText(rows.size() + " saved");
    }

    public static class ProfileRow {
        private final String id;
        private final String type;
        private final String name;
        private final LocalDate dobValue;
        private final String phone;
        private final String email;

        private ProfileRow(String id, String type, String name, LocalDate dobValue, String phone, String email) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.dobValue = dobValue;
            this.phone = phone;
            this.email = email;
        }

        public static ProfileRow fromPatient(Patient patient) {
            return new ProfileRow(
                    patient.getId(),
                    "Patient",
                    patient.getFullName(),
                    patient.getDateOfBirth(),
                    patient.getPhone(),
                    patient.getEmail()
            );
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getDob() {
            return dobValue == null ? "" : dobValue.toString();
        }

        public LocalDate getDobValue() {
            return dobValue;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }
    }
}
