package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> getAll();
    List<Book> getByValue(String value);
    Book save(Book book) throws ValidationException;
    Book update(Book book);
    void delete(Long id);


    Book findById(Long id);

    List<Book> getByAuthor(Author dbAuthor);

    List<Book> getByISBN(String isbn);
}
