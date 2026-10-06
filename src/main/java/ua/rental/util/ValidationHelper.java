package ua.rental.util;

import java.util.Set;

class ValidationHelper {

    private ValidationHelper() {}

    static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("Очікувалось не-null значення для '" + fieldName + "', але отримано: null");
        }
    }

    static void requireNonBlank(String value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Очікувався непорожній рядок для '" + fieldName + "', але отримано: порожній рядок");
        }
    }

    static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException("Очікувалось додатне значення (> 0) для '" + fieldName + "', але отримано: " + value);
        }
    }

    static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException("Очікувалось неот'ємне значення (>= 0) для '" + fieldName + "', але отримано: " + value);
        }
    }

    static String validateEnum(String value, Set<String> allowedValues, String fieldName) {
        requireNonBlank(value, fieldName);
        String normalized = value.trim().toUpperCase();
        if (!allowedValues.contains(normalized)) {
            throw new IllegalArgumentException("Очікувалось одне з значень " + allowedValues + " для '" + fieldName + "', але отримано: '" + value + "'");
        }
        return normalized;
    }
}
