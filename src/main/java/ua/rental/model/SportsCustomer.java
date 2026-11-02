package ua.rental.model;

import ua.rental.util.RentalUtils;

public record SportsCustomer(String passportNumber, String fullName, String phone) {

    public SportsCustomer {
        RentalUtils.requireNonBlank(passportNumber, "passportNumber");
        RentalUtils.requireNonBlank(fullName, "fullName");
        RentalUtils.requireNonBlank(phone, "phone");
    }

    public static SportsCustomer of(String passportNumber, String fullName, String phone) {
        return new SportsCustomer(passportNumber, fullName, phone);
    }
}
