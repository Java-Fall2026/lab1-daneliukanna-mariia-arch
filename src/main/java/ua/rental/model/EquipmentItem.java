package ua.rental.model;

import ua.common.BaseEntity;
import ua.rental.util.RentalUtils;

import java.util.Objects;

public class EquipmentItem extends BaseEntity {

    private final String inventoryNumber; // 🔑
    private final String name;
    private final EquipmentCategory category;
    private double pricePerDay; // ✎
    private EquipmentCondition condition;   // ✎

    public EquipmentItem(String inventoryNumber, String name, EquipmentCategory category, double pricePerDay, EquipmentCondition condition) {
        super();
        RentalUtils.requireNonBlank(inventoryNumber, "inventoryNumber");
        RentalUtils.requireNonBlank(name, "name");
        RentalUtils.requirePositive(pricePerDay, "pricePerDay");
        RentalUtils.requireNonNull(category, "category");
        RentalUtils.requireNonNull(condition, "condition");

        this.inventoryNumber = inventoryNumber;
        this.name = name;
        this.category = category;
        this.pricePerDay = pricePerDay;
        this.condition = condition;
    }

    public String getInventoryNumber() { return inventoryNumber; }
    public String getName() { return name; }
    public EquipmentCategory getCategory() { return category; }
    public double getPricePerDay() { return pricePerDay; }
    public EquipmentCondition getCondition() { return condition; }

    public final void setPricePerDay(double pricePerDay) {
        RentalUtils.requirePositive(pricePerDay, "pricePerDay");
        this.pricePerDay = pricePerDay;
    }

    public final void setCondition(EquipmentCondition condition) {
        RentalUtils.requireNonNull(condition, "condition");
        this.condition = condition;
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
                ", category=" + category +
                ", pricePerDay=" + pricePerDay +
                ", condition=" + condition +
                ", createdAt=" + createdAt +
                '}';
    }
}
