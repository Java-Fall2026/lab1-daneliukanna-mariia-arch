package ua.rental.model;

import ua.common.BaseEntity;
import ua.rental.util.RentalUtils;

import java.time.LocalDate;
import java.util.Objects;

public class RentalAgreement extends BaseEntity {

    private final SportsCustomer customer; // part of 🔑❓
    private final EquipmentItem item;       // part of 🔑❓
    private final LocalDate rentStartDate; // part of 🔑❓
    private final LocalDate rentEndDate;
    private final double depositPaid;

    public RentalAgreement(SportsCustomer customer, EquipmentItem item, LocalDate rentStartDate, LocalDate rentEndDate, double depositPaid) {
        super();
        RentalUtils.requireNonNull(customer, "customer");
        RentalUtils.requireNonNull(item, "item");
        RentalUtils.requireNonNull(rentStartDate, "rentStartDate");
        RentalUtils.requireNonNull(rentEndDate, "rentEndDate");
        RentalUtils.requireNonNegative(depositPaid, "depositPaid");

        if (rentEndDate.isBefore(rentStartDate)) {
            throw new IllegalArgumentException("Дата закінчення оренди (" + rentEndDate + ") не може бути раніше дати початку (" + rentStartDate + ")");
        }

        this.customer = customer;
        this.item = item;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.depositPaid = depositPaid;
    }

    public SportsCustomer getCustomer() { return customer; }
    public EquipmentItem getItem() { return item; }
    public LocalDate getRentStartDate() { return rentStartDate; }
    public LocalDate getRentEndDate() { return rentEndDate; }
    public double getDepositPaid() { return depositPaid; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalAgreement that = (RentalAgreement) o;
        return Objects.equals(customer, that.customer) &&
                Objects.equals(item, that.item) &&
                Objects.equals(rentStartDate, that.rentStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, item, rentStartDate);
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "customer=" + customer.getFullName() +
                ", item=" + item.getName() +
                ", rentStartDate=" + rentStartDate +
                ", rentEndDate=" + rentEndDate +
                ", depositPaid=" + depositPaid +
                ", createdAt=" + createdAt +
                '}';
    }
}
