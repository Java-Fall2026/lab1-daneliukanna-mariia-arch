package ua.rental.model;

import ua.rental.util.RentalUtils;
import java.time.LocalDate;

public record ReturnInspection(
        String inspectionId,
        RentalAgreement agreement,
        LocalDate returnDate,
        double damageFee
) {
    public ReturnInspection {
        RentalUtils.requireNonBlank(inspectionId, "inspectionId");
        RentalUtils.requireNonNull(agreement, "agreement");
        RentalUtils.requireNonNull(returnDate, "returnDate");
        RentalUtils.requireNonNegative(damageFee, "damageFee");

        if (returnDate.isBefore(agreement.rentStartDate())) {
            throw new IllegalArgumentException("Дата огляду не може бути раніше дати початку оренди");
        }
    }

    public static ReturnInspection of(String inspectionId, RentalAgreement agreement, LocalDate returnDate, double damageFee) {
        return new ReturnInspection(inspectionId, agreement, returnDate, damageFee);
    }
}
