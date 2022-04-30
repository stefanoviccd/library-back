package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;

public interface BookValidator {
    void validate(Object o) throws ValidationException;
}
