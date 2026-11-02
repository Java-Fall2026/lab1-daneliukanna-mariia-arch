package ua.rental.model;

/**
 * Represents the condition of sports equipment and its price adjustment factor.
 */
public enum EquipmentCondition {
    NEW(1.2),         // New (+20% base price)
    GOOD(1.0),        // Good condition (base price)
    FAIR(0.8),        // Fair condition (-20% discount)
    MAINTENANCE(0.5); // Requires maintenance (-50% discount)

    private final double discountFactor;

    EquipmentCondition(double discountFactor) {
        this.discountFactor = discountFactor;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }
}
