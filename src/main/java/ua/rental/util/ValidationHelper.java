package ua.rental.util;

public class ValidationHelper {

    private ValidationHelper() {}

    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("Expected a non-null value for '" + fieldName + "', but received: null");
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Expected a non-blank string for '" + fieldName + "', but received: an empty string");
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException("Expected a positive value (> 0) for '" + fieldName + "', but received: " + value);
        }
    }

    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException("Expected a non-negative value (>= 0) for '" + fieldName + "', but received: " + value);
        }
    }


}
