package ua.rental.util;

import ua.rental.model.RentalAgreement;

import java.time.temporal.ChronoUnit;
import java.util.Set;

public class RentalUtils {

    private RentalUtils() {}

    // --- Методи-делегати для валідації ---

    public static void requireNonNull(Object obj, String fieldName) {
        ValidationHelper.requireNonNull(obj, fieldName);
    }

    public static void requireNonBlank(String value, String fieldName) {
        ValidationHelper.requireNonBlank(value, fieldName);
    }

    public static void requirePositive(double value, String fieldName) {
        ValidationHelper.requirePositive(value, fieldName);
    }

    public static void requireNonNegative(double value, String fieldName) {
        ValidationHelper.requireNonNegative(value, fieldName);
    }

    public static String validateEnum(String value, Set<String> allowedValues, String fieldName) {
        return ValidationHelper.validateEnum(value, allowedValues, fieldName);
    }

    // --- Обчислювальні методи з варіанта ---

    public static long rentalDays(RentalAgreement agreement) {
        requireNonNull(agreement, "agreement");
        long days = ChronoUnit.DAYS.between(agreement.getRentStartDate(), agreement.getRentEndDate());
        return days == 0 ? 1 : days;
    }

    public static double totalCost(RentalAgreement agreement) {
        requireNonNull(agreement, "agreement");
        long days = rentalDays(agreement);
        return days * agreement.getItem().getPricePerDay();
    }
}
