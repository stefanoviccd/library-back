package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;

import javax.persistence.EntityManager;

public interface BookValidator {
    void validate(Object o, EntityManager em
    ) throws ValidationException;
}
