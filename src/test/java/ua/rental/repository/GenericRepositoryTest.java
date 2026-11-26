package ua.rental.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;
import ua.rental.model.SportsCustomer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenericRepositoryTest {
    private GenericRepository<SportsCustomer> customers() {
        return new GenericRepository<>(SportsCustomer.class, SportsCustomer::passportNumber);
    }

    @Test
    @DisplayName("Add and find returns the added item")
    void addAndFind() {
        var repository = customers();
        var customer = new SportsCustomer("P-1", "Anna", "123");
        repository.add(customer);
        assertEquals(customer, repository.findByIdentity("P-1").orElseThrow());
    }

    @Test
    @DisplayName("Missing identity returns an empty optional")
    void missingFind() {
        assertTrue(customers().findByIdentity("missing").isEmpty());
    }

    @Test
    @DisplayName("Adding a duplicate throws the domain exception")
    void duplicateAdd() {
        var repository = customers();
        repository.add(new SportsCustomer("P-1", "Anna", "123"));
        DuplicateIdentityException exception = assertThrows(DuplicateIdentityException.class,
                () -> repository.add(new SportsCustomer("P-1", "Other", "456")));
        assertTrue(exception.getMessage().contains("P-1"));
        assertTrue(exception.getMessage().contains("SportsCustomer"));
    }

    @Test
    @DisplayName("Add if absent rejects duplicates without changing size")
    void addIfAbsent() {
        var repository = customers();
        repository.add(new SportsCustomer("P-1", "Anna", "123"));
        assertFalse(repository.addIfAbsent(new SportsCustomer("P-1", "Other", "456")));
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Remove returns true only when an item existed")
    void removeExistingAndMissing() {
        var repository = customers();
        repository.add(new SportsCustomer("P-1", "Anna", "123"));
        assertTrue(repository.removeByIdentity("P-1"));
        assertFalse(repository.removeByIdentity("P-1"));
    }

    @Test
    @DisplayName("Find all cannot mutate repository state")
    void findAllIsUnmodifiable() {
        var repository = customers();
        repository.add(new SportsCustomer("P-1", "Anna", "123"));
        List<SportsCustomer> result = repository.findAll();
        assertThrows(UnsupportedOperationException.class, result::clear);
        assertEquals(1, repository.size());
    }

    @Test
    @DisplayName("Generic repository supports different entity types")
    void supportsDifferentTypes() {
        GenericRepository<SportsCustomer> customers = customers();
        GenericRepository<EquipmentItem> equipment = new GenericRepository<>(EquipmentItem.class,
                EquipmentItem::getInventoryNumber);
        customers.add(new SportsCustomer("P-1", "Anna", "123"));
        equipment.add(new EquipmentItem("E-1", "Bike", EquipmentCategory.BICYCLE, 100,
                EquipmentCondition.GOOD));
        assertEquals("Anna", customers.findByIdentity("P-1").orElseThrow().fullName());
        assertEquals("Bike", equipment.findByIdentity("E-1").orElseThrow().getName());
    }

    @Test
    @DisplayName("Null items and blank identities are rejected")
    void rejectsInvalidItems() {
        var repository = customers();
        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
        GenericRepository<SportsCustomer> invalid = new GenericRepository<>(SportsCustomer.class,
                customer -> " ");
        assertThrows(IllegalArgumentException.class,
                () -> invalid.add(new SportsCustomer("P-1", "Anna", "123")));
    }
}
