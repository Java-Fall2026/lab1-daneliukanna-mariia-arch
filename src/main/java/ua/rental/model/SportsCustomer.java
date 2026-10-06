package ua.rental.model;

import ua.common.BaseEntity;
import ua.rental.util.RentalUtils;

import java.util.Objects;

public class SportsCustomer extends BaseEntity {

    private final String passportNumber; // 🔑
    private final String fullName;
    private final String phone;

    private SportsCustomer(String passportNumber, String fullName, String phone) {
        super();
        RentalUtils.requireNonBlank(passportNumber, "passportNumber");
        RentalUtils.requireNonBlank(fullName, "fullName");
        RentalUtils.requireNonBlank(phone, "phone");

        this.passportNumber = passportNumber;
        this.fullName = fullName;
        this.phone = phone;
    }

    public static SportsCustomer of(String passportNumber, String fullName, String phone) {
        return new SportsCustomer(passportNumber, fullName, phone);
    }

    public String getPassportNumber() { return passportNumber; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SportsCustomer that = (SportsCustomer) o;
        return Objects.equals(passportNumber, that.passportNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber);
    }

    @Override
    public String toString() {
        return "SportsCustomer{" +
                "passportNumber='" + passportNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
