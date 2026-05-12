module com.meditrack {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.desktop;

    exports com.meditrack;
    opens com.meditrack.ui to javafx.fxml, javafx.base;
}
