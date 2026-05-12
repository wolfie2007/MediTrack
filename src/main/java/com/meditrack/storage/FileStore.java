package com.meditrack.storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStore<T> {
    private final Path filePath;

    public FileStore(Path filePath) {
        this.filePath = filePath;
    }

    public List<T> load() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(filePath))) {
            Object data = input.readObject();
            @SuppressWarnings("unchecked")
            List<T> result = (List<T>) data;
            return result;
        } catch (IOException | ClassNotFoundException ex) {
            return new ArrayList<>();
        }
    }

    public void save(List<T> items) {
        try {
            Files.createDirectories(filePath.getParent());
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                output.writeObject(items);
            }
        } catch (IOException ex) {
            // Keep app running even if persistence fails.
        }
    }
}
