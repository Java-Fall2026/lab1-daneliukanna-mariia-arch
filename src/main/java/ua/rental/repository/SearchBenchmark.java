
package ua.rental.repository;

import ua.rental.model.EquipmentCategory;
import ua.rental.model.EquipmentCondition;
import ua.rental.model.EquipmentItem;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SearchBenchmark {

    private static final int REPEATS = 7;
    private static final int WARMUP_ROUNDS = 3;

    private static volatile Object sink;

    public static void main(String[] args) {

        LoggerContext loggerContext =
                (LoggerContext) LoggerFactory.getILoggerFactory();

        loggerContext.getLogger("ROOT").setLevel(Level.OFF);

        IdentityExtractor<EquipmentItem> extractor =
                EquipmentItem::getInventoryNumber;

        GenericRepository<EquipmentItem> repository =
                new GenericRepository<>(EquipmentItem.class, extractor);

        // Populate repository with 100,000 objects
        for (int i = 0; i < 100_000; i++) {
            repository.add(new EquipmentItem(
                    "BENCH-%06d".formatted(i),
                    "Equipment " + i,
                    EquipmentCategory.BICYCLE,
                    100.0,
                    EquipmentCondition.GOOD
            ));
        }

        // Take one snapshot before measurements
        List<EquipmentItem> list = repository.findAll();

        String[] identities = {
                "BENCH-000000",
                "BENCH-099999",
                "MISSING-IDENTITY"
        };

        String[] scenarios = {
                "First",
                "Last",
                "Missing"
        };

        System.out.println("=== Search Benchmark ===");
        System.out.println("Number of objects: " + list.size());
        System.out.println("Measurement repetitions: " + REPEATS);
        System.out.println();

        for (int i = 0; i < identities.length; i++) {

            String identity = identities[i];

            // Warm-up before measurements
            for (int warm = 0; warm < WARMUP_ROUNDS; warm++) {
                sink = repository.findByIdentity(identity);
                sink = linearSearch(list, identity, extractor);
            }

            // Measure Map search
            long[] mapResults = measure(
                    () -> repository.findByIdentity(identity)
            );

            // Measure linear search
            long[] listResults = measure(
                    () -> linearSearch(list, identity, extractor)
            );

            System.out.printf(
                    "%s:%n", scenarios[i]
            );

            System.out.printf(
                    "  Map: median=%d ns, min=%d ns%n",
                    mapResults[0], mapResults[1]
            );

            System.out.printf(
                    "  List: median=%d ns, min=%d ns%n",
                    listResults[0], listResults[1]
            );

            System.out.println();
        }
    }

    private static long[] measure(Runnable operation) {

        List<Long> times = new ArrayList<>();

        for (int i = 0; i < REPEATS; i++) {

            long start = System.nanoTime();

            operation.run();

            long elapsed = System.nanoTime() - start;

            times.add(elapsed);
        }

        Collections.sort(times);

        long median = times.get(times.size() / 2);
        long min = times.get(0);

        return new long[]{median, min};
    }

    private static <E> Optional<E> linearSearch(
            List<E> items,
            String identity,
            IdentityExtractor<E> extractor
    ) {

        for (E item : items) {

            if (identity.equals(extractor.identityOf(item))) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }
}