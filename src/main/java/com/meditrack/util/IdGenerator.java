package com.meditrack.util;

import java.util.UUID;

public class IdGenerator {
    private IdGenerator() {
    }

    public static String newId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
