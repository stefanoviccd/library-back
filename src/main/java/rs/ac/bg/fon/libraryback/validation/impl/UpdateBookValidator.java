package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.BookValidator;

import javax.persistence.EntityManager;
import java.util.List;

public class UpdateBookValidator implements BookValidator {
    private BookRepository bookRepository;
    public UpdateBookValidator(){
        bookRepository=new BookRepositoryImpl();
    }
    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        Book book=(Book) o;
        if (book == null) {
            throw new ValidationException("Knjiga za izmenu je null!");
        }
        if (book.getId() == null) throw new ValidationException("Knjiga za izmenu ima id null!");


    }


}
