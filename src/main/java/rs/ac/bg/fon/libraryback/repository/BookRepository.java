package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;

import javax.persistence.EntityManager;
import java.util.List;

public interface BookRepository {
    List<Book> getAll(EntityManager em);

    List<Book> getByValue(String value, EntityManager em);

    Book save(Book book, EntityManager em) throws ValidationException;

    Book update(Book book, EntityManager em);

    void delete(Long id, EntityManager em);


    Book findById(Long id, EntityManager em);

    List<Book> getByAuthor(Author dbAuthor, EntityManager em);

    List<Book> getByISBN(String isbn, EntityManager em);

    int getTitleCount(EntityManager em);
}
