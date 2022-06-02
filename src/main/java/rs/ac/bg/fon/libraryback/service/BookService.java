package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;

import javax.persistence.EntityManager;
import java.util.List;

public interface BookService {
    List<BookDTO> getAllBooks() ;
    void deleteBook(Long id) throws ValidationException;

    BookDTO save(BookDTO book) throws ValidationException;

     List<BookDTO> getByValue(String value);

    BookDTO update(BookDTO book) throws ValidationException;

    BookDTO getById(Long id) throws ValidationException;
}
