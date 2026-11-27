package ua.rental;

import ua.rental.loader.EquipmentLoader;
import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;
import ua.rental.model.RentalAgreement;
import ua.rental.model.ReturnInspection;
import ua.rental.model.SportsCustomer;
import ua.rental.repository.DuplicateIdentityException;
import ua.rental.repository.GenericRepository;

import java.nio.file.Path;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Три незалежні репозиторії ===");
        GenericRepository<EquipmentItem> equipmentRepository = new GenericRepository<>(
                EquipmentItem.class, EquipmentItem::getInventoryNumber);
        EquipmentItem bike = new EquipmentItem("DEMO-001", "Гірський велосипед", EquipmentCategory.BICYCLE,
                500.0, EquipmentCondition.GOOD);
        equipmentRepository.add(bike);
        System.out.println("Пошук DEMO-001: " + equipmentRepository.findByIdentity("DEMO-001").isPresent());
        System.out.println("Пошук UNKNOWN: " + equipmentRepository.findByIdentity("UNKNOWN").isPresent());

        GenericRepository<SportsCustomer> customerRepository = new GenericRepository<>(
                SportsCustomer.class, SportsCustomer::passportNumber);
        customerRepository.add(new SportsCustomer("AA123456", "Олена Коваль", "+380501112233"));
        System.out.println("Клієнт AA123456: " + customerRepository.findByIdentity("AA123456").orElse(null));

        SportsCustomer customer = new SportsCustomer("BB654321", "Іван Петренко", "+380671234567");
        RentalAgreement agreement = new RentalAgreement(customer, bike, LocalDate.now(), LocalDate.now().plusDays(2), 300.0);
        GenericRepository<RentalAgreement> agreementRepository = new GenericRepository<>(RentalAgreement.class,
                value -> value.customer().passportNumber() + "-" + value.item().getInventoryNumber() + "-" + value.rentStartDate());
        agreementRepository.add(agreement);
        String agreementId = customer.passportNumber() + "-" + bike.getInventoryNumber() + "-" + agreement.rentStartDate();
        System.out.println("Договір знайдено: " + agreementRepository.findByIdentity(agreementId).isPresent());

        GenericRepository<ReturnInspection> inspectionRepository = new GenericRepository<>(
                ReturnInspection.class, ReturnInspection::inspectionId);
        inspectionRepository.add(new ReturnInspection("INSP-001", agreement, LocalDate.now().plusDays(2), 0));
        System.out.println("Огляд INSP-001: " + inspectionRepository.findByIdentity("INSP-001").isPresent());

        System.out.println("\n=== Завантаження CSV у репозиторій ===");
        EquipmentLoader loader = new EquipmentLoader();
        var loadedEquipment = loader.load(Path.of("data/equipment.csv"));
        GenericRepository<EquipmentItem> csvRepository = new GenericRepository<>(EquipmentItem.class,
                EquipmentItem::getInventoryNumber);
        for (EquipmentItem item : loadedEquipment) csvRepository.addIfAbsent(item);
        System.out.println("З репозиторію завантажено позицій: " + csvRepository.size());

        System.out.println("\n=== Контракт дублікатів ===");
        try {
            csvRepository.add(new EquipmentItem("SKI-001", "Дублікат лиж", EquipmentCategory.SKI,
                    400.0, EquipmentCondition.GOOD));
        } catch (DuplicateIdentityException exception) {
            System.out.println("add перехопив виняток: " + exception.getMessage());
        }
        boolean added = csvRepository.addIfAbsent(new EquipmentItem("SKI-001", "Ще одні лижі",
                EquipmentCategory.SKI, 410.0, EquipmentCondition.GOOD));
        System.out.println("addIfAbsent додав дублікат: " + added);

        System.out.println("\n=== Видалення ===");
        System.out.println("Видалення SKI-001: " + csvRepository.removeByIdentity("SKI-001"));
        System.out.println("Видалення ABSENT: " + csvRepository.removeByIdentity("ABSENT"));
        System.out.println("Підсумковий розмір: " + csvRepository.size());
    }
}
