package com.meditrack;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("TestApp starting...");
            
            Label label = new Label("MediTrack is Working!");
            label.setStyle("-fx-font-size: 24; -fx-text-fill: white;");
            
            VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-background-color: #0F1419;");
            root.getChildren().add(label);
            
            Scene scene = new Scene(root, 400, 300);
            
            primaryStage.setTitle("MediTrack Test");
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.toFront();
            primaryStage.requestFocus();
            
            System.out.println("TestApp window should be visible now!");
        } catch (Exception e) {
            System.err.println("ERROR in TestApp:");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("TestApp main launching...");
        launch(args);
    }
}
