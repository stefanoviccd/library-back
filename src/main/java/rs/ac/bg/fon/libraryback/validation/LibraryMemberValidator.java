package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.ValidationException;

public interface LibraryMemberValidator {
    void validate(Object o) throws ValidationException;
}
