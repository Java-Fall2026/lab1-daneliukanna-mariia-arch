package ua.rental.util;

public class ValidationHelper {

    private ValidationHelper() {}

    public static void requireNonNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException("Очікувалось не-null значення для '" + fieldName + "', але отримано: null");
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        requireNonNull(value, fieldName);
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Очікувався непорожній рядок для '" + fieldName + "', але отримано: порожній рядок");
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException("Очікувалось додатне значення (> 0) для '" + fieldName + "', але отримано: " + value);
        }
    }

    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException("Очікувалось неот'ємне значення (>= 0) для '" + fieldName + "', але отримано: " + value);
        }
    }


}
