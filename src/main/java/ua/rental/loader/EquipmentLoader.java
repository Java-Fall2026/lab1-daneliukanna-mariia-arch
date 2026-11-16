package ua.rental.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.rental.exception.InvalidDataException;
import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Loads independent sports equipment records from a CSV file. */
public class EquipmentLoader {
    private static final Logger log = LoggerFactory.getLogger(EquipmentLoader.class);
    private static final String HEADER = "inventoryNumber,name,category,pricePerDay,condition";

    public List<EquipmentItem> load(Path path) {
        List<EquipmentItem> equipment = new ArrayList<>();
        int lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            lineNumber++;
            if (!HEADER.equals(header)) {
                throw new InvalidDataException("Line 1: expected CSV header: " + HEADER);
            }
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    equipment.add(parseLine(line, lineNumber));
                } catch (InvalidDataException e) {
                    log.warn("Skipping invalid CSV row: {}", e.getMessage());
                }
            }
            log.info("Loaded {} equipment items", equipment.size());
        } catch (IOException | SecurityException e) {
            log.error("Could not read equipment file {}: {}", path, e.getMessage());
        } catch (InvalidDataException e) {
            log.error("Invalid equipment CSV {}: {}", path, e.getMessage());
        } finally {
            log.info("Equipment CSV processing finished: {}", path);
        }
        return equipment;
    }

    EquipmentItem parseLine(String line, int lineNumber) throws InvalidDataException {
        try {
            String[] fields = line.split(",", -1);
            if (fields.length != 5) {
                throw new IllegalArgumentException("Expected 5 comma-separated fields");
            }
            return new EquipmentItem(
                    fields[0].trim(), fields[1].trim(),
                    EquipmentCategory.valueOf(fields[2].trim()),
                    Double.parseDouble(fields[3].trim()),
                    EquipmentCondition.valueOf(fields[4].trim())
            );
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new InvalidDataException("Line " + lineNumber + ": " + e.getMessage(), e);
        }
    }
}
