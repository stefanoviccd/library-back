package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.BookValidator;

import javax.persistence.EntityManager;
import java.util.List;
public class AddBookValidator implements BookValidator {

    private BookRepository bookRepository;
    public AddBookValidator(){
        bookRepository=new BookRepositoryImpl();
    }

    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        Book book=(Book) o;
        if (book == null) throw new ValidationException("Knjiga za čuvanje je null!");
        if (book.getAuthor() == null) {
            throw new ValidationException("Knjiga za čuvanje nema autora!");
        }
        if (book.getAuthor().getName() == null || book.getAuthor().getLastName() == null) {
            throw new ValidationException("Autor za čuvanje nema validno ime ili prezime!");
        }
        if(bookWithSameISBNExists(book, em))
            throw new ValidationException("Knjiga sa istim ISBN brojem postoji u sistemu!");
    }

    private boolean bookWithSameISBNExists(Book book, EntityManager em) {
        List<Book> dbResult=bookRepository.getByISBN(book.getISBN(), em);
        return !dbResult.isEmpty();

    }
}
