package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.validation.BookValidator;

import javax.persistence.EntityManager;
import java.util.List;
public class DeleteBookValidator implements BookValidator {
    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        Long id=(Long) o;
        if (id == null) throw new ValidationException("Knjiga za brisanje ne sme imati id null!");
        Book dbBook=em.find(Book.class, id);
        if(dbBook.isCurrentlyRented())
            throw  new ValidationException("Knjiga je trenutno iznajmljena!");




    }
}
