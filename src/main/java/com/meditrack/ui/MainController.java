package com.meditrack.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;

public class MainController {
    @FXML
    private StackPane contentHost;
    @FXML
    private Button dashboardButton;
    @FXML
    private Button patientsButton;
    @FXML
    private Button medicinesButton;
    @FXML
    private Button prescriptionsButton;
    @FXML
    private Button doseTrackerButton;
    @FXML
    private Button reportsButton;

    @FXML
    private void initialize() {
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadView("/fxml/dashboard.fxml", dashboardButton);
    }

    @FXML
    private void showPatients() {
        loadView("/fxml/patients.fxml", patientsButton);
    }

    @FXML
    private void showMedicines() {
        loadView("/fxml/medicines.fxml", medicinesButton);
    }

    @FXML
    private void showPrescriptions() {
        loadView("/fxml/prescriptions.fxml", prescriptionsButton);
    }

    @FXML
    private void showDoseTracker() {
        loadView("/fxml/dose-tracker.fxml", doseTrackerButton);
    }

    @FXML
    private void showReports() {
        loadView("/fxml/reports.fxml", reportsButton);
    }

    private void loadView(String fxmlPath, Button activeButton) {
        try {
            System.out.println("Loading view: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentHost.getChildren().setAll(view);
            setActiveButton(activeButton);
            System.out.println("Successfully loaded: " + fxmlPath);
        } catch (IOException ex) {
            System.err.println("ERROR loading FXML " + fxmlPath + ": " + ex.getMessage());
            ex.printStackTrace();
            contentHost.getChildren().clear();
        }
    }

    private void setActiveButton(Button activeButton) {
        List<Button> buttons = List.of(
                dashboardButton,
                patientsButton,
                medicinesButton,
                prescriptionsButton,
                doseTrackerButton,
                reportsButton
        );
        for (Button button : buttons) {
            button.getStyleClass().remove("nav-button-active");
        }
        if (activeButton != null) {
            activeButton.getStyleClass().add("nav-button-active");
        }
    }
}
