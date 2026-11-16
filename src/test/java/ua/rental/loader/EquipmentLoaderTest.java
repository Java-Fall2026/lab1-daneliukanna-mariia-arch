package ua.rental.loader;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.rental.exception.InvalidDataException;
import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EquipmentLoaderTest {
    @TempDir Path tempDir;

    @Test @DisplayName("Creates a valid equipment item")
    void createsValidItem() {
        EquipmentItem item = new EquipmentItem("S-1", "Skis", EquipmentCategory.SKI, 350, EquipmentCondition.GOOD);
        assertEquals("S-1", item.getInventoryNumber());
        assertEquals(350, item.getPricePerDay());
    }

    @Test @DisplayName("Rejects a CSV row with an invalid category and preserves cause")
    void rejectsInvalidCategory() {
        EquipmentLoader loader = new EquipmentLoader();
        InvalidDataException ex = assertThrows(InvalidDataException.class,
                () -> loader.parseLine("X-1,Item,UNKNOWN,100,GOOD", 7));
        assertTrue(ex.getMessage().contains("Line 7"));
        assertNotNull(ex.getCause());
    }

    @Test @DisplayName("Rejects a CSV row with an invalid price")
    void rejectsInvalidPrice() {
        InvalidDataException ex = assertThrows(InvalidDataException.class,
                () -> new EquipmentLoader().parseLine("X-1,Item,SKI,abc,GOOD", 3));
        assertTrue(ex.getMessage().contains("Line 3"));
    }

    @Test @DisplayName("Rejects a CSV row with an incorrect number of fields")
    void rejectsWrongFieldCount() {
        assertThrows(InvalidDataException.class,
                () -> new EquipmentLoader().parseLine("X-1,Item,SKI,100", 4));
    }

    @Test @DisplayName("Loads valid rows and skips invalid rows")
    void loadsValidRowsAndSkipsInvalidRows() throws Exception {
        Path csv = tempDir.resolve("equipment.csv");
        Files.writeString(csv, "inventoryNumber,name,category,pricePerDay,condition\n" +
                "S-1,Skis,SKI,100,GOOD\n" + "X-2,Board,UNKNOWN,100,GOOD\n" +
                "B-3,Bike,BICYCLE,200,NEW\n");
        List<EquipmentItem> result = new EquipmentLoader().load(csv);
        assertEquals(2, result.size());
    }

    @Test @DisplayName("Missing file does not stop the application")
    void missingFileReturnsEmptyList() {
        assertTrue(new EquipmentLoader().load(tempDir.resolve("missing.csv")).isEmpty());
    }

    @Test @DisplayName("Condition setter rejects null")
    void conditionSetterRejectsNull() {
        EquipmentItem item = new EquipmentItem("S-1", "Skis", EquipmentCategory.SKI, 100, EquipmentCondition.GOOD);
        assertThrows(IllegalArgumentException.class, () -> item.setCondition(null));
    }

    @Test @DisplayName("Rental duration and total cost are calculated correctly")
    void calculatesRental() {
        var customer = new ua.rental.model.SportsCustomer("123", "Test User", "+380000000000");
        var item = new EquipmentItem("S-1", "Skis", EquipmentCategory.SKI, 100, EquipmentCondition.GOOD);
        var agreement = new ua.rental.model.RentalAgreement(customer, item,
                java.time.LocalDate.of(2026, 1, 1), java.time.LocalDate.of(2026, 1, 4), 0);
        assertEquals(3, ua.rental.util.RentalUtils.rentalDays(agreement));
        assertEquals(360.0, ua.rental.util.RentalUtils.totalCost(agreement));
    }
}
