package ua.rental.repository;

public class DuplicateIdentityException extends IllegalStateException {
    public DuplicateIdentityException(String identity, String typeName) {
        super("Duplicate identity '" + identity + "' for type " + typeName);
    }
}
