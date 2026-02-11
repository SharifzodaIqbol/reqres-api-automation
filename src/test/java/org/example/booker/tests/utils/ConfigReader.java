package org.example.booker.tests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();
    private static final String NOT_FOUND =  "Not found: ";
    static {
        loadProperties(properties, "message.properties");
    }

    private static void loadProperties(Properties prop, String fileName) {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException(NOT_FOUND + fileName);
            }
            prop.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(fileName, ex);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            String errorMsg = properties.getProperty("error.prop.missing");
            throw new RuntimeException(String.format(errorMsg, key));
        }
        return value;
    }

    public static String getMessage(String key, Object... args) {
        String text = properties.getProperty(key);
        if (text == null) {
            return properties.getProperty("error.key.not_found");
        }
        return String.format(text, args);
    }
}