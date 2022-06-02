package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.ValidationException;

import javax.persistence.EntityManager;

public interface AuthorValidator {
    void validate(Object o, EntityManager em) throws ValidationException;
}
