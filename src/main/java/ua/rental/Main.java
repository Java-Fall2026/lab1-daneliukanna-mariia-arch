package ua.rental;

import ua.rental.model.*;
import ua.rental.util.RentalUtils;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Створення record-обʼєкта та звернення до його компонентів ===");
        SportsCustomer customer1 = SportsCustomer.of("123456789", "Іван Петренко", "+380971112233");

        // Accessing record components via generated accessor methods
        System.out.println("Паспорт клієнта: " + customer1.passportNumber());
        System.out.println("ПІБ клієнта: " + customer1.fullName());
        System.out.println("Телефон: " + customer1.phone());

        System.out.println("\n=== 2. Enum у роботі ===");
        // a) Iterating through values() and accessing custom enum field via getter
        System.out.println("Усі доступні стани обладнання та їхні коефіцієнти:");
        for (EquipmentCondition cond : EquipmentCondition.values()) {
            System.out.println(" - " + cond.name() + ": discountFactor = " + cond.getDiscountFactor());
        }

        // b) Handling invalid valueOf() in a try-catch block
        try {
            System.out.println("\nСпроба виклику EquipmentCategory.valueOf(\"INVALID_CATEGORY\")...");
            EquipmentCategory invalidCategory = EquipmentCategory.valueOf("INVALID_CATEGORY");
        } catch (IllegalArgumentException e) {
            System.out.println("Піймано очікувану помилку (valueOf): " + e.getMessage());
        }

        System.out.println("\n=== 3. Перевірка обох switch-виразів ===");
        EquipmentItem item = new EquipmentItem("INV-SKI-001", "Лижі Alpine 170см", EquipmentCategory.SKI, 350.0, EquipmentCondition.NEW);

        // Testing 1st switch expression (all enum constants covered, no default)
        double conditionMult = RentalUtils.getConditionMultiplier(item.getCondition());
        System.out.println("1-й switch (множник за станом NEW): " + conditionMult);

        // Testing 2nd switch expression (category price rate)
        double categoryRate = RentalUtils.getCategoryPriceRate(item.getCategory());
        System.out.println("2-й switch (коефіцієнт категорії SKI): " + categoryRate);

        System.out.println("\n=== 4. Створення record з невалідними даними (компактний конструктор) ===");
        try {
            System.out.println("Спроба створити RentalAgreement з неправильними датами оренди...");
            new RentalAgreement(customer1, item, LocalDate.now(), LocalDate.now().minusDays(2), 500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Піймано очікувану помилку з компактного конструктора: " + e.getMessage());
        }

        System.out.println("\n=== 5. Порівняння двох однакових record-обʼєктів (equals та hashCode) ===");
        SportsCustomer customer2 = SportsCustomer.of("123456789", "Іван Петренко", "+380971112233");

        System.out.println("customer1.equals(customer2): " + customer1.equals(customer2));
        System.out.println("customer1.hashCode() == customer2.hashCode(): " + (customer1.hashCode() == customer2.hashCode()));

        System.out.println("\n=== 6. Вивід record через згенерований toString() ===");
        RentalAgreement validAgreement = new RentalAgreement(customer1, item, LocalDate.now(), LocalDate.now().plusDays(3), 1000.0);

        System.out.println("Автоматичний toString(): " + validAgreement);
    }
}
