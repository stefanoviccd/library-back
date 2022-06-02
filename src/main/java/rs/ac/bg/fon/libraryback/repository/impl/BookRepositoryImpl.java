package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.validation.BookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.AddBookValidator;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {


    public BookRepositoryImpl() {

    }

    @Override
    public List<Book> getAll(EntityManager em) {
        List<Book> dbResult = em.createQuery("select m from Book m")
                .getResultList();
        return dbResult;

    }

    @Override
    public List<Book> getByValue(String value, EntityManager em) {
        String searchingParameter = "%" + value + "%";
        List<Book> books = em.createQuery("select m from Book m where m.ISBN LIKE :value or  m.title LIKE :value or m.author.name LIKE :value or m.author.lastName LIKE :value").setParameter("value", searchingParameter)
                .getResultList();
        return books;


    }

    public List<Book> getByISBN(String isbn, EntityManager em) {
        String searchingParameter = isbn;
        List<Book> books = em.createQuery("select m from Book m where m.ISBN LIKE :value").setParameter("value", searchingParameter)
                .getResultList();
        return books;


    }

    @Override
    public int getTitleCount(EntityManager em) {
        List<String> titles = em.createQuery("select distinct m.title from Book m")
                .getResultList();
        return titles.size();


    }

    @Override
    public Book save(Book book, EntityManager em) {
        em.persist(book);
        return book;


    }

    @Override
    public Book update(Book book, EntityManager em) {
        Book dbBook = em.find(Book.class, book.getId());
        em.merge(book);
        return dbBook;
    }

    @Override
    public void delete(Long id, EntityManager em) {
        Book dbBook = em.find(Book.class, id);
        em.remove(dbBook);
    }

    @Override
    public Book findById(Long id, EntityManager em) {
        Book dbBook = em.find(Book.class, id);
        return dbBook;
    }

    @Override
    public List<Book> getByAuthor(Author dbAuthor, EntityManager em) {
        List<Book> dbResult = em.createQuery("select m from Book m where m.author.id= :id").setParameter("id", dbAuthor.getId()).getResultList();
        return dbResult;

    }
}
