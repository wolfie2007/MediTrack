package com.meditrack.storage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppState {
    private static final AppState INSTANCE = new AppState();
    private final DataRepository repository;
    private final Path dataDir;

    private AppState() {
        dataDir = Paths.get(System.getProperty("user.home"), ".meditrack");
        repository = new DataRepository(dataDir);
    }

    public static AppState getInstance() {
        return INSTANCE;
    }

    public DataRepository getRepository() {
        return repository;
    }

    public Path getDataDir() {
        return dataDir;
    }

    public void initialize() {
        repository.loadAll();
    }

    public void shutdown() {
        repository.saveAll();
    }

    private boolean isEmpty() {
        return repository.getPatients().isEmpty()
                && repository.getMedicines().isEmpty()
                && repository.getPrescriptions().isEmpty()
                && repository.getDoseLogs().isEmpty();
    }
}
