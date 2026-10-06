package ua.rental;

import ua.rental.model.*;
import ua.rental.util.RentalUtils;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. Успішні сценарії під час роботи з доменом ===");

        // Створення клієнта через фабричний метод of(...)
        SportsCustomer customer = SportsCustomer.of("123456789", "Іван Петренко", "+380971112233");

        // Створення майна
        EquipmentItem ski = new EquipmentItem("INV-SKI-001", "Alpine Skis 170cm", "ski", 350.0, "new");
        System.out.println("Створено майно: " + ski);

        // Зміна полів з позначкою ✎
        ski.setPricePerDay(400.0);
        ski.setCondition("GOOD");

        // Договір оренди на 3 дні
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(3);
        RentalAgreement agreement = new RentalAgreement(customer, ski, start, end, 1000.0);
        System.out.println("Створено угоду: " + agreement);

        // Обчислювальні методи з RentalUtils
        System.out.println("Кількість днів оренди: " + RentalUtils.rentalDays(agreement));
        System.out.println("Загальна вартість оренди: " + RentalUtils.totalCost(agreement) + " грн");

        // Огляд при поверненні через of(...)
        ReturnInspection inspection = ReturnInspection.of("INSP-101", agreement, end, 0.0);
        System.out.println("Акт огляду: " + inspection);

        System.out.println("\n=== 2. Демонстрація невдалих сценаріїв (Помилки валідації) ===");

        try {
            System.out.println("Тест 1: Невалідна категорія...");
            new EquipmentItem("INV-002", "Велосипед", "UNKNOWN_CATEGORY", 100.0, "NEW");
        } catch (IllegalArgumentException e) {
            System.out.println("Піймано очікувану помилку: " + e.getMessage());
        }

        try {
            System.out.println("Тест 2: Дата кінця оренди раніше дати початку...");
            new RentalAgreement(customer, ski, LocalDate.now(), LocalDate.now().minusDays(1), 500.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Піймано очікувану помилку: " + e.getMessage());
        }

        try {
            System.out.println("Тест 3: Негативна ціна оренди...");
            ski.setPricePerDay(-50.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Піймано очікувану помилку: " + e.getMessage());
        }
    }
}
