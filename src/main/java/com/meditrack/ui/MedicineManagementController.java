package com.meditrack.ui;

import com.meditrack.model.Injection;
import com.meditrack.model.Medicine;
import com.meditrack.model.Syrup;
import com.meditrack.model.Tablet;
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

public class MedicineManagementController {
    @FXML
    private TableView<MedicineRow> medicineTable;
    @FXML
    private TableColumn<MedicineRow, String> typeColumn;
    @FXML
    private TableColumn<MedicineRow, String> nameColumn;
    @FXML
    private TableColumn<MedicineRow, String> dosageColumn;
    @FXML
    private TableColumn<MedicineRow, String> stockColumn;
    @FXML
    private TableColumn<MedicineRow, String> durationColumn;
    @FXML
    private Label countLabel;
    @FXML
    private ChoiceBox<String> typeChoice;
    @FXML
    private TextField nameField;
    @FXML
    private TextField dosageField;
    @FXML
    private TextField stockField;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField extraField;

    private final ObservableList<MedicineRow> rows = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        typeChoice.getItems().setAll("Tablet", "Syrup", "Injection");
        typeChoice.setValue("Tablet");

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        medicineTable.setItems(rows);
        medicineTable.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                fillForm(selected);
                typeChoice.setDisable(true);
            }
        });

        // refresh when repository changes
        EventBus.register(() -> Platform.runLater(this::refresh));
        refresh();
    }

    @FXML
    private void saveMedicine() {
        String name = nameField.getText().trim();
        String dosage = dosageField.getText().trim();
        Integer stock = parseStock(stockField.getText());
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String extra = extraField.getText().trim();

        if (name.isEmpty()) {
            AlertUtil.showError("Missing name", "Please enter a medicine name.");
            return;
        }
        if (stock == null) {
            AlertUtil.showError("Invalid stock", "Stock must be a number.");
            return;
        }

        DataRepository repository = AppState.getInstance().getRepository();
        MedicineRow selected = medicineTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            Optional<Medicine> medicine = repository.getMedicines().stream()
                    .filter(item -> item.getId().equals(selected.getId()))
                    .findFirst();
            medicine.ifPresent(item -> {
                item.setName(name);
                item.setDosage(dosage);
                item.setStock(stock);
                item.setStartDate(startDate);
                item.setEndDate(endDate);
                updateExtra(item, extra);
            });
        } else {
            Medicine medicine = createMedicine(typeChoice.getValue(), name, dosage, stock, startDate, endDate, extra);
            repository.getMedicines().add(medicine);
        }

        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void deleteMedicine() {
        MedicineRow selected = medicineTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        DataRepository repository = AppState.getInstance().getRepository();
        repository.getMedicines().removeIf(item -> item.getId().equals(selected.getId()));
        repository.saveAll();
        clearForm();
        refresh();
    }

    @FXML
    private void clearForm() {
        medicineTable.getSelectionModel().clearSelection();
        typeChoice.setValue("Tablet");
        nameField.clear();
        dosageField.clear();
        stockField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        extraField.clear();
        typeChoice.setDisable(false);
    }

    private Medicine createMedicine(String type, String name, String dosage, int stock, LocalDate startDate, LocalDate endDate, String extra) {
        String id = IdGenerator.newId("med");
        if ("Syrup".equals(type)) {
            return new Syrup(id, name, dosage, stock, startDate, endDate, extra);
        }
        if ("Injection".equals(type)) {
            return new Injection(id, name, dosage, stock, startDate, endDate, extra);
        }
        return new Tablet(id, name, dosage, stock, startDate, endDate, extra);
    }

    private Integer parseStock(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void updateExtra(Medicine medicine, String extra) {
        if (medicine instanceof Tablet tablet) {
            tablet.setStrength(extra);
        } else if (medicine instanceof Syrup syrup) {
            syrup.setFlavor(extra);
        } else if (medicine instanceof Injection injection) {
            injection.setSite(extra);
        }
    }

    private void fillForm(MedicineRow row) {
        typeChoice.setValue(row.getType());
        nameField.setText(row.getName());
        dosageField.setText(row.getDosage());
        stockField.setText(row.getStock());
        startDatePicker.setValue(row.getStartDate());
        endDatePicker.setValue(row.getEndDate());
        extraField.setText(row.getExtra());
    }

    private void refresh() {
        DataRepository repository = AppState.getInstance().getRepository();
        rows.clear();
        for (Medicine medicine : repository.getMedicines()) {
            rows.add(MedicineRow.fromMedicine(medicine));
        }
        rows.sort(Comparator.comparing(MedicineRow::getName));
        countLabel.setText(rows.size() + " saved");
    }

    public static class MedicineRow {
        private final String id;
        private final String type;
        private final String name;
        private final String dosage;
        private final String stock;
        private final String duration;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String extra;

        private MedicineRow(String id, String type, String name, String dosage, String stock, String duration, LocalDate startDate, LocalDate endDate, String extra) {
            this.id = id;
            this.type = type;
            this.name = name;
            this.dosage = dosage;
            this.stock = stock;
            this.duration = duration;
            this.startDate = startDate;
            this.endDate = endDate;
            this.extra = extra;
        }

        public static MedicineRow fromMedicine(Medicine medicine) {
            String duration = "";
            if (medicine.getStartDate() != null && medicine.getEndDate() != null) {
                duration = medicine.getStartDate() + " to " + medicine.getEndDate();
            }
            String extra = "";
            if (medicine instanceof Tablet tablet) {
                extra = tablet.getStrength();
            } else if (medicine instanceof Syrup syrup) {
                extra = syrup.getFlavor();
            } else if (medicine instanceof Injection injection) {
                extra = injection.getSite();
            }
            return new MedicineRow(
                    medicine.getId(),
                    medicine.getType(),
                    medicine.getName(),
                    medicine.getDosage(),
                    String.valueOf(medicine.getStock()),
                    duration,
                    medicine.getStartDate(),
                    medicine.getEndDate(),
                    extra
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

        public String getDosage() {
            return dosage;
        }

        public String getStock() {
            return stock;
        }

        public String getDuration() {
            return duration;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getExtra() {
            return extra;
        }
    }
}
