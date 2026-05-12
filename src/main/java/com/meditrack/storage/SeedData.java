package com.meditrack.storage;

import com.meditrack.model.*;
import com.meditrack.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SeedData {
    private SeedData() {
    }

    public static void populate(DataRepository repository) {
        Patient user = new Patient(
                IdGenerator.newId("pat"),
                "You",
                "0300-5551111",
                "user@example.com",
                LocalDate.of(1995, 5, 15)
        );

        Medicine med1 = new Tablet(
                IdGenerator.newId("med"),
                "Aspirin",
                "500 mg",
                20,
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "500 mg"
        );
        Medicine med2 = new Tablet(
                IdGenerator.newId("med"),
                "Vitamin D",
                "1000 IU",
                30,
                LocalDate.now(),
                LocalDate.now().plusDays(60),
                "1000 IU"
        );
        Medicine med3 = new Syrup(
                IdGenerator.newId("med"),
                "Cough Syrup",
                "10 ml",
                4,
                LocalDate.now(),
                LocalDate.now().plusDays(7),
                "Honey"
        );

        Prescription rx1 = new Prescription(
                IdGenerator.newId("rx"),
                user.getId(),
                med1.getId()
        );
        rx1.setInstructions("Take in morning with breakfast.");
        rx1.setStartDate(LocalDate.now());
        rx1.setEndDate(LocalDate.now().plusDays(30));

        Prescription rx2 = new Prescription(
                IdGenerator.newId("rx"),
                user.getId(),
                med2.getId()
        );
        rx2.setInstructions("Take once daily.");
        rx2.setStartDate(LocalDate.now());
        rx2.setEndDate(LocalDate.now().plusDays(60));

        DoseLog log1 = new DoseLog(IdGenerator.newId("dose"), rx1.getId(), LocalDateTime.now().withHour(8).withMinute(0));
        log1.setStatus(DoseLog.DoseStatus.TAKEN);
        log1.setTakenAt(LocalDateTime.now().withHour(8).withMinute(15));

        repository.getPatients().add(user);
        repository.getMedicines().add(med1);
        repository.getMedicines().add(med2);
        repository.getMedicines().add(med3);
        repository.getPrescriptions().add(rx1);
        repository.getPrescriptions().add(rx2);
        repository.getDoseLogs().add(log1);
    }
}
