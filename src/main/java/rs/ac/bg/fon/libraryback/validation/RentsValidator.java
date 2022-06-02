package rs.ac.bg.fon.libraryback.validation;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.LibraryMember;

import javax.persistence.EntityManager;

public interface RentsValidator {
    void validate(LibraryMember member, Book book, EntityManager em) throws ValidationException;
}
