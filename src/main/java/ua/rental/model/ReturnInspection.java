package ua.rental.model;

import ua.common.BaseEntity;
import ua.rental.util.RentalUtils;

import java.time.LocalDate;
import java.util.Objects;

public class ReturnInspection extends BaseEntity {

    private final String inspectionId; // 🔑
    private final RentalAgreement agreement;
    private final LocalDate returnDate;
    private final double damageFee;

    private ReturnInspection(String inspectionId, RentalAgreement agreement, LocalDate returnDate, double damageFee) {
        super();
        RentalUtils.requireNonBlank(inspectionId, "inspectionId");
        RentalUtils.requireNonNull(agreement, "agreement");
        RentalUtils.requireNonNull(returnDate, "returnDate");
        RentalUtils.requireNonNegative(damageFee, "damageFee");

        if (returnDate.isBefore(agreement.getRentStartDate())) {
            throw new IllegalArgumentException("Дата повернення (" + returnDate + ") не може бути раніше дати початку оренди (" + agreement.getRentStartDate() + ")");
        }

        this.inspectionId = inspectionId;
        this.agreement = agreement;
        this.returnDate = returnDate;
        this.damageFee = damageFee;
    }

    public static ReturnInspection of(String inspectionId, RentalAgreement agreement, LocalDate returnDate, double damageFee) {
        return new ReturnInspection(inspectionId, agreement, returnDate, damageFee);
    }

    public String getInspectionId() { return inspectionId; }
    public RentalAgreement getAgreement() { return agreement; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getDamageFee() { return damageFee; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnInspection that = (ReturnInspection) o;
        return Objects.equals(inspectionId, that.inspectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inspectionId);
    }

    @Override
    public String toString() {
        return "ReturnInspection{" +
                "inspectionId='" + inspectionId + '\'' +
                ", agreement=" + agreement +
                ", returnDate=" + returnDate +
                ", damageFee=" + damageFee +
                ", createdAt=" + createdAt +
                '}';
    }
}
