package ua.rental.model;

import ua.common.BaseEntity;
import ua.rental.util.RentalUtils;

import java.util.Objects;
import java.util.Set;

public class EquipmentItem extends BaseEntity {

    private static final Set<String> ALLOWED_CATEGORIES = Set.of("SKI", "SNOWBOARD", "BICYCLE", "KAYAK");
    private static final Set<String> ALLOWED_CONDITIONS = Set.of("NEW", "GOOD", "FAIR", "MAINTENANCE");

    private final String inventoryNumber; // 🔑
    private final String name;
    private final String category;
    private double pricePerDay; // ✎
    private String condition;   // ✎

    public EquipmentItem(String inventoryNumber, String name, String category, double pricePerDay, String condition) {
        super();
        RentalUtils.requireNonBlank(inventoryNumber, "inventoryNumber");
        RentalUtils.requireNonBlank(name, "name");
        RentalUtils.requirePositive(pricePerDay, "pricePerDay");

        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.category = RentalUtils.validateEnum(category, ALLOWED_CATEGORIES, "category");
        this.pricePerDay = pricePerDay;
        this.condition = RentalUtils.validateEnum(condition, ALLOWED_CONDITIONS, "condition");
    }

    public String getInventoryNumber() { return inventoryNumber; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPricePerDay() { return pricePerDay; }
    public String getCondition() { return condition; }

    public final void setPricePerDay(double pricePerDay) {
        RentalUtils.requirePositive(pricePerDay, "pricePerDay");
        this.pricePerDay = pricePerDay;
    }

    public final void setCondition(String condition) {
        this.condition = RentalUtils.validateEnum(condition, ALLOWED_CONDITIONS, "condition");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipmentItem that = (EquipmentItem) o;
        return Objects.equals(inventoryNumber, that.inventoryNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventoryNumber);
    }

    @Override
    public String toString() {
        return "EquipmentItem{" +
                "inventoryNumber='" + inventoryNumber + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", pricePerDay=" + pricePerDay +
                ", condition='" + condition + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
