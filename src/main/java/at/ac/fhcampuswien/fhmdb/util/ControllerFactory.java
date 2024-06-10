package at.ac.fhcampuswien.fhmdb.util;

import javafx.util.Callback;
import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private static ControllerFactory instance;
    private final Map<Class<?>, Object> cache = new HashMap<>();

    private ControllerFactory() {}

    public static synchronized ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }
        return instance;
    }

    @Override
    public Object call(Class<?> type) {
        System.out.println("Using Factory for: " + type.getName());
        return cache.computeIfAbsent(type, k -> {
            try {
                System.out.println("Creating new Controller for: " + type.getName());
                return type.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Could not create controller for " + type.getName(), e);
            }
        });
    }
}


