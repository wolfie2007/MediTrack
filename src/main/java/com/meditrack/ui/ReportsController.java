package com.meditrack.ui;

import com.meditrack.model.DoseLog;
import com.meditrack.storage.AppState;
import com.meditrack.storage.DataRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportsController {
    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMMM yyyy");

    @FXML
    private TextArea reportArea;
    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        refreshReport();
    }

    @FXML
    private void exportReport() {
        String report = reportArea.getText();
        if (report.isEmpty()) {
            return;
        }
        Path reportPath = AppState.getInstance().getDataDir()
                .resolve("health-report-" + YearMonth.now() + ".txt");
        try {
            Files.createDirectories(reportPath.getParent());
            Files.writeString(reportPath, report);
            statusLabel.setText("Saved to " + reportPath.getFileName());
        } catch (IOException ex) {
            statusLabel.setText("Export failed");
        }
    }

    private void refreshReport() {
        DataRepository repository = AppState.getInstance().getRepository();
        List<DoseLog> logs = repository.getDoseLogs();

        YearMonth currentMonth = YearMonth.now();
        LocalDate start = currentMonth.atDay(1);
        LocalDate end = currentMonth.atEndOfMonth();

        List<DoseLog> monthLogs = logs.stream()
                .filter(log -> log.getScheduledAt() != null)
                .filter(log -> {
                    LocalDate date = log.getScheduledAt().toLocalDate();
                    return !date.isBefore(start) && !date.isAfter(end);
                })
                .toList();

        long total = monthLogs.size();
        long taken = monthLogs.stream().filter(log -> log.getStatus() == DoseLog.DoseStatus.TAKEN).count();
        long missed = monthLogs.stream().filter(log -> log.getStatus() == DoseLog.DoseStatus.MISSED).count();
        double adherence = total == 0 ? 0 : (taken * 100.0 / total);

        StringBuilder builder = new StringBuilder();
        builder.append("MediTrack - Monthly Adherence Report").append("\n");
        builder.append("Period: ").append(currentMonth.format(MONTH_FORMAT)).append("\n\n");
        builder.append("Total doses scheduled: ").append(total).append("\n");
        builder.append("Taken: ").append(taken).append("\n");
        builder.append("Missed: ").append(missed).append("\n");
        builder.append("Adherence: ").append(String.format("%.1f", adherence)).append("%\n\n");
        builder.append("Notes:").append("\n");
        builder.append("- Review missed doses and adjust reminders.").append("\n");
        builder.append("- Check low stock medicines weekly.").append("\n");

        reportArea.setText(builder.toString());
        statusLabel.setText("Updated " + LocalDate.now());
    }
}
