package com.company.config;

import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found");
            PROPS.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) {
        String v = PROPS.getProperty(key);
        if (v == null || v.isBlank()) throw new IllegalArgumentException("Missing config key: " + key);
        return v.trim();
    }

    private AppConfig() {}
}
