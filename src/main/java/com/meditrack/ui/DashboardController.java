package com.meditrack.ui;

import com.meditrack.model.DoseLog;
import com.meditrack.model.Medicine;
import com.meditrack.model.Patient;
import com.meditrack.model.Prescription;
import com.meditrack.storage.AppState;
import com.meditrack.storage.DataRepository;
import com.meditrack.util.EventBus;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    @FXML
    private Label todayLabel;
    @FXML
    private Label streakLabel;
    @FXML
    private Label dosesTodayLabel;
    @FXML
    private Label dosesTodayMeta;
    @FXML
    private Label lowStockLabel;
    @FXML
    private Label scheduleMeta;
    @FXML
    private TableView<ScheduleRow> scheduleTable;
    @FXML
    private TableColumn<ScheduleRow, String> timeColumn;
    @FXML
    private TableColumn<ScheduleRow, String> patientColumn;
    @FXML
    private TableColumn<ScheduleRow, String> medicineColumn;
    @FXML
    private TableColumn<ScheduleRow, String> doseColumn;
    @FXML
    private TableColumn<ScheduleRow, String> statusColumn;

    @FXML
    private void initialize() {
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patient"));
        medicineColumn.setCellValueFactory(new PropertyValueFactory<>("medicine"));
        doseColumn.setCellValueFactory(new PropertyValueFactory<>("dose"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // register for repository change events
        EventBus.register(() -> Platform.runLater(this::refresh));
        refresh();
    }

    private void refresh() {
        DataRepository repository = AppState.getInstance().getRepository();
        List<DoseLog> doseLogs = repository.getDoseLogs();
        List<Medicine> medicines = repository.getMedicines();
        List<Prescription> prescriptions = repository.getPrescriptions();
        List<Patient> patients = repository.getPatients();

        LocalDate today = LocalDate.now();
        todayLabel.setText(today.format(DATE_FORMAT));

        List<DoseLog> todayLogs = doseLogs.stream()
                .filter(log -> log.getScheduledAt() != null && log.getScheduledAt().toLocalDate().equals(today))
                .sorted(Comparator.comparing(DoseLog::getScheduledAt))
                .toList();

        long takenCount = todayLogs.stream()
                .filter(log -> log.getStatus() == DoseLog.DoseStatus.TAKEN)
                .count();

        long dueCount = todayLogs.stream()
                .filter(log -> log.getStatus() != DoseLog.DoseStatus.TAKEN)
                .count();

        dosesTodayLabel.setText(String.valueOf(todayLogs.size()));
        dosesTodayMeta.setText(takenCount + " taken, " + dueCount + " due");
        scheduleMeta.setText(todayLogs.size() + " doses planned");

        long lowStock = medicines.stream()
                .filter(med -> med.getStock() <= 5)
                .count();
        lowStockLabel.setText(String.valueOf(lowStock));

        streakLabel.setText(calculateStreak(doseLogs) + " days");

        Map<String, Prescription> prescriptionMap = prescriptions.stream()
                .collect(Collectors.toMap(Prescription::getId, prescription -> prescription));
        Map<String, Medicine> medicineMap = medicines.stream()
                .collect(Collectors.toMap(Medicine::getId, medicine -> medicine));
        Map<String, Patient> patientMap = patients.stream()
                .collect(Collectors.toMap(Patient::getId, patient -> patient));

        ObservableList<ScheduleRow> rows = FXCollections.observableArrayList();
        for (DoseLog log : todayLogs) {
            Prescription prescription = prescriptionMap.get(log.getPrescriptionId());
            Medicine medicine = prescription == null ? null : medicineMap.get(prescription.getMedicineId());
            Patient patient = prescription == null ? null : patientMap.get(prescription.getPatientId());
            String time = log.getScheduledAt() == null ? "" : log.getScheduledAt().format(TIME_FORMAT);
            rows.add(new ScheduleRow(
                    time,
                    patient == null ? "" : patient.getDisplayLabel(),
                    medicine == null ? "" : medicine.getName(),
                    medicine == null ? "" : medicine.getDosage(),
                    log.getStatus().name()
            ));
        }
        scheduleTable.setItems(rows);
    }

    private int calculateStreak(List<DoseLog> doseLogs) {
        Map<LocalDate, List<DoseLog>> byDate = doseLogs.stream()
                .filter(log -> log.getScheduledAt() != null)
                .collect(Collectors.groupingBy(log -> log.getScheduledAt().toLocalDate()));

        int streak = 0;
        LocalDate cursor = LocalDate.now();
        while (true) {
            List<DoseLog> logs = byDate.get(cursor);
            if (logs == null || logs.isEmpty()) {
                break;
            }
            boolean allTaken = logs.stream().allMatch(log -> log.getStatus() == DoseLog.DoseStatus.TAKEN);
            if (!allTaken) {
                break;
            }
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    public static class ScheduleRow {
        private final String time;
        private final String patient;
        private final String medicine;
        private final String dose;
        private final String status;

        public ScheduleRow(String time, String patient, String medicine, String dose, String status) {
            this.time = time;
            this.patient = patient;
            this.medicine = medicine;
            this.dose = dose;
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public String getPatient() {
            return patient;
        }

        public String getMedicine() {
            return medicine;
        }

        public String getDose() {
            return dose;
        }

        public String getStatus() {
            return status;
        }
    }
}
