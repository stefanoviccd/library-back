package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.BookValidator;

import java.util.List;
public class AddBookValidator implements BookValidator {
    private BookRepository bookRepository;
    public AddBookValidator(){
        bookRepository=new BookRepositoryImpl();
    }

    @Override
    public void validate(Object o) throws ValidationException {
        Book book=(Book) o;
        if(bookWithSameISBMExists(book))
            throw new ValidationException("Knjiga sa istim ISBN brojem postoji u sistemu!");
    }

    private boolean bookWithSameISBMExists(Book book) {
        List<Book> dbResult=bookRepository.getByISBN(book.getISBN());
        return !dbResult.isEmpty();

    }
}
