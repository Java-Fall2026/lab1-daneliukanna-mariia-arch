package ua.rental.model;

import ua.rental.util.RentalUtils;
import java.time.LocalDate;

public record RentalAgreement(
        SportsCustomer customer,
        EquipmentItem item,
        LocalDate rentStartDate,
        LocalDate rentEndDate,
        double depositPaid
) {
    public RentalAgreement {
        RentalUtils.requireNonNull(customer, "customer");
        RentalUtils.requireNonNull(item, "item");
        RentalUtils.requireNonNull(rentStartDate, "rentStartDate");
        RentalUtils.requireNonNull(rentEndDate, "rentEndDate");
        RentalUtils.requireNonNegative(depositPaid, "depositPaid");

        if (rentEndDate.isBefore(rentStartDate)) {
            throw new IllegalArgumentException("Rental end date (" + rentEndDate + ") cannot be before start date (" + rentStartDate + ")");
        }
    }
}
