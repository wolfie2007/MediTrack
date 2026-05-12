package com.meditrack.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {
    public interface Listener {
        void onDataChanged();
    }

    private static final List<Listener> listeners = new CopyOnWriteArrayList<>();

    public static void register(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public static void unregister(Listener listener) {
        listeners.remove(listener);
    }

    public static void publishDataChanged() {
        for (Listener listener : listeners) {
            try {
                listener.onDataChanged();
            } catch (Throwable ignored) { }
        }
    }
}
