package ua.rental.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenericRepository<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);
    private final Class<T> type;
    private final IdentityExtractor<T> extractor;
    private final Map<String, T> items = new LinkedHashMap<>();

    public GenericRepository(Class<T> type, IdentityExtractor<T> extractor) {
        if (type == null || extractor == null) {
            throw new IllegalArgumentException("Type and identity extractor must not be null");
        }
        this.type = type;
        this.extractor = extractor;
    }

    public void add(T item) {
        String identity = requireIdentity(item);
        if (items.containsKey(identity)) {
            DuplicateIdentityException exception = new DuplicateIdentityException(identity, type.getSimpleName());
            log.warn("Rejected duplicate identity '{}' for {}", identity, type.getSimpleName());
            log.error("Repository add failed: {}", exception.getMessage());
            throw exception;
        }
        items.put(identity, item);
        log.info("Added {} with identity '{}'", type.getSimpleName(), identity);
    }

    public boolean addIfAbsent(T item) {
        String identity = requireIdentity(item);
        if (items.containsKey(identity)) {
            log.warn("Rejected duplicate identity '{}' for {}", identity, type.getSimpleName());
            return false;
        }
        items.put(identity, item);
        log.info("Added {} with identity '{}'", type.getSimpleName(), identity);
        return true;
    }

    public void addAll(Collection<T> values) {
        if (values == null) throw new IllegalArgumentException("Items collection must not be null");
        for (T item : values) add(item);
    }

    public boolean removeByIdentity(String identity) {
        if (identity == null || identity.isBlank()) throw new IllegalArgumentException("Identity must not be blank");
        T removed = items.remove(identity);
        if (removed == null) {
            log.warn("No {} found for identity '{}'", type.getSimpleName(), identity);
            return false;
        }
        log.info("Removed {} with identity '{}'", type.getSimpleName(), identity);
        return true;
    }

    public Optional<T> findByIdentity(String identity) {
        if (identity == null || identity.isBlank()) throw new IllegalArgumentException("Identity must not be blank");
        T found = items.get(identity);
        if (found == null) log.warn("No {} found for identity '{}'", type.getSimpleName(), identity);
        else log.info("Found {} with identity '{}'", type.getSimpleName(), identity);
        return Optional.ofNullable(found);
    }

    public List<T> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(items.values()));
    }

    public int size() { return items.size(); }

    private String requireIdentity(T item) {
        if (item == null) throw new IllegalArgumentException("Item must not be null");
        String identity = extractor.identityOf(item);
        if (identity == null || identity.isBlank()) throw new IllegalArgumentException("Item identity must not be blank");
        return identity;
    }
}
