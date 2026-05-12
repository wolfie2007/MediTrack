package com.meditrack;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import com.meditrack.storage.AppState;

public class App extends Application {
    @Override
    public void init() {
        AppState.getInstance().initialize();
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            logToFile("Loading FXML...");
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/main.fxml"));
            logToFile("Creating scene...");
            Scene scene = new Scene(loader.load(), 1100, 700);
            logToFile("Loading CSS...");
            scene.getStylesheets().add(App.class.getResource("/css/theme.css").toExternalForm());
            logToFile("Setting stage title...");
            stage.setTitle("MediTrack - Personal Health Management System");
            logToFile("Setting scene...");
            stage.setScene(scene);
            
            // Explicitly set stage properties to ensure visibility
            stage.setWidth(1100);
            stage.setHeight(700);
            stage.setX(100);
            stage.setY(100);
            stage.setOpacity(1.0);
            stage.setResizable(true);
            stage.setAlwaysOnTop(false);
            
            logToFile("About to call stage.show()...");
            stage.show();
            logToFile("stage.show() called, now calling toFront()...");
            Platform.runLater(() -> {
                stage.toFront();
                stage.requestFocus();
                logToFile("stage.toFront() and requestFocus() completed!");
            });
            logToFile("stage.show() completed successfully!");
        } catch (Exception e) {
            logToFile("ERROR starting application: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    private static void logToFile(String message) {
        try {
            String logFile = System.getProperty("user.home") + "\\.meditrack\\debug.log";
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(System.getProperty("user.home") + "\\.meditrack"));
            java.nio.file.Files.write(
                java.nio.file.Paths.get(logFile),
                (message + "\n").getBytes(),
                java.nio.file.StandardOpenOption.CREATE,
                java.nio.file.StandardOpenOption.APPEND
            );
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        AppState.getInstance().shutdown();
    }
}
