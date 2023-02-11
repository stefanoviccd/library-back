package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;

public interface IValidator {
    void validate(Object o) throws ValidationException, UserAlreadyExistsException;
}
