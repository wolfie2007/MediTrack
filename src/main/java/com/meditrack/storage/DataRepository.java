package com.meditrack.storage;

import com.meditrack.model.*;
import com.meditrack.util.EventBus;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private final FileStore<Patient> patientStore;
    private final FileStore<Medicine> medicineStore;
    private final FileStore<Prescription> prescriptionStore;
    private final FileStore<DoseLog> doseLogStore;

    private List<Patient> patients = new ArrayList<>();
    private List<Medicine> medicines = new ArrayList<>();
    private List<Prescription> prescriptions = new ArrayList<>();
    private List<DoseLog> doseLogs = new ArrayList<>();

    public DataRepository(Path baseDir) {
        this.patientStore = new FileStore<>(baseDir.resolve("patients.dat"));
        this.medicineStore = new FileStore<>(baseDir.resolve("medicines.dat"));
        this.prescriptionStore = new FileStore<>(baseDir.resolve("prescriptions.dat"));
        this.doseLogStore = new FileStore<>(baseDir.resolve("dose-logs.dat"));
    }

    public void loadAll() {
        patients = patientStore.load();
        medicines = medicineStore.load();
        prescriptions = prescriptionStore.load();
        doseLogs = doseLogStore.load();
    }

    public void saveAll() {
        patientStore.save(patients);
        medicineStore.save(medicines);
        prescriptionStore.save(prescriptions);
        doseLogStore.save(doseLogs);
        // Notify listeners that repository data changed
        EventBus.publishDataChanged();
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public List<DoseLog> getDoseLogs() {
        return doseLogs;
    }
}
