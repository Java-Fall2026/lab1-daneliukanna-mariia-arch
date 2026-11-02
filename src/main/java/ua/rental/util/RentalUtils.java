package ua.rental.util;

import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.RentalAgreement;

import java.time.temporal.ChronoUnit;

/**
 * Utility class containing business operations and validation helpers.
 */
public class RentalUtils {

    private RentalUtils() {}

    // --- Validation delegate methods ---

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

    // --- Switch Expression 1: Covers all enum constants without default ---

    /**
     * Calculates the condition multiplier for pricing based on equipment condition.
     *
     * @param condition equipment condition enum value
     * @return multiplier value
     */
    public static double getConditionMultiplier(EquipmentCondition condition) {
        requireNonNull(condition, "condition");
        return switch (condition) {
            case NEW -> 1.2;
            case GOOD -> 1.0;
            case FAIR -> 0.8;
            case MAINTENANCE -> 0.5;
        };
    }

    // --- Switch Expression 2: Returns category price rate ---

    /**
     * Determines category price multiplier.
     *
     * @param category equipment category enum value
     * @return price rate coefficient
     */
    public static double getCategoryPriceRate(EquipmentCategory category) {
        requireNonNull(category, "category");
        return switch (category) {
            case SKI, SNOWBOARD -> 1.2;
            case BICYCLE -> 1.0;
            default -> 0.8;
        };
    }

    // --- Domain calculation methods ---

    public static long rentalDays(RentalAgreement agreement) {
        requireNonNull(agreement, "agreement");
        long days = ChronoUnit.DAYS.between(agreement.rentStartDate(), agreement.rentEndDate());
        return days == 0 ? 1 : days;
    }

    public static double totalCost(RentalAgreement agreement) {
        requireNonNull(agreement, "agreement");
        long days = rentalDays(agreement);
        double baseCost = days * agreement.item().getPricePerDay();

        double conditionFactor = getConditionMultiplier(agreement.item().getCondition());
        double categoryFactor = getCategoryPriceRate(agreement.item().getCategory());

        return baseCost * conditionFactor * categoryFactor;
    }
}
