package ua.rental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.rental.loader.EquipmentLoader;
import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;

import java.nio.file.Path;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        EquipmentLoader loader = new EquipmentLoader();

        System.out.println("=== Завантаження спортивного обладнання ===");
        var equipment = loader.load(Path.of("data/equipment.csv"));
        equipment.forEach(item -> System.out.println(item));
        System.out.println("Успішно завантажено позицій: " + equipment.size());

        System.out.println("\n=== Спроба прочитати відсутній файл ===");
        var missing = loader.load(Path.of("data/missing-equipment.csv"));
        System.out.println("Кількість завантажених позицій із відсутнього файла: " + missing.size());

        System.out.println("\n=== Перевірка некоректного значення в сеттері ===");
        EquipmentItem item = new EquipmentItem("DEMO-001", "Гірські лижі", EquipmentCategory.SKI,
                350.0, EquipmentCondition.GOOD);
        try {
            item.setPricePerDay(-100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилку оброблено: " + e.getMessage());
            log.warn("Rejected invalid equipment price for inventory number {}", item.getInventoryNumber());
        }

        log.info("Application demonstration completed");
        log.error("Demonstration of ERROR-level logging");
    }
}
