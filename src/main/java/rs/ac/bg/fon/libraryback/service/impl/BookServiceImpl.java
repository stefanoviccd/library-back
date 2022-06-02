package rs.ac.bg.fon.libraryback.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.repository.AuthorRepository;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.service.BookService;
import rs.ac.bg.fon.libraryback.validation.BookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.AddBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.DeleteBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateBookValidator;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRentRepository rentRepository;
    private BookValidator deleteValidator;
    private BookValidator addBookValidator;
    private BookValidator updateBookValidator;
    @Autowired
    private ModelMapper modelMapper;


    public BookServiceImpl() {
        deleteValidator = new DeleteBookValidator();
        addBookValidator = new AddBookValidator();
        updateBookValidator = new UpdateBookValidator();
    }

    public List<BookDTO> getAllBooks() {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<Book> dbResult = bookRepository.getAll(em);
            em.getTransaction().commit();
            return dbResult.stream().map(r->modelMapper.map(r, BookDTO.class)).collect(Collectors.toList());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }


    }

    public void deleteBook(Long id) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            deleteValidator.validate(id, em);
            Author dbAuthor = getBookAuthor(id, em);
            List<BookRent> bookRents = rentRepository.getByBook(id, em);
            for (BookRent rent : bookRents) {
                em.remove(rent);

            }
            bookRepository.delete(id, em);
            if (!areAuthorsBooksPresentedInDatabase(dbAuthor, em)) authorRepository.delete(dbAuthor, em);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }

    }

    private boolean areAuthorsBooksPresentedInDatabase(Author dbAuthor, EntityManager em) {

        List<Book> authorsBook = bookRepository.getByAuthor(dbAuthor, em);
        return !authorsBook.isEmpty();
    }

    private Author getBookAuthor(Long id, EntityManager em) {
        return bookRepository.findById(id, em).getAuthor();
    }



    public BookDTO save(BookDTO book) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        if (!em.getTransaction().isActive()) em.getTransaction().begin();
        try {
            addBookValidator.validate(modelMapper.map(book, Book.class), em);

            List<Author> authors = authorRepository.getByFullName(book.getAuthor().getName(), book.getAuthor().getLastName(), em);

            if (authors.isEmpty()) {
                authorRepository.save(book.getAuthor(), em);
            } else {
                book.setAuthor(authors.get(0));
            }
            bookRepository.save(modelMapper.map(book, Book.class), em);
            em.getTransaction().commit();
            return book;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }

    }

    public List<BookDTO> getByValue(String value) {

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<Book> dbResult;
        try {
            if (value == null || value.isEmpty()) dbResult = bookRepository.getAll(em);
            else dbResult = bookRepository.getByValue(value, em);
            em.getTransaction().commit();
            return dbResult.stream().map(r->modelMapper.map(r, BookDTO.class)).collect(Collectors.toList());

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }

    }

    public BookDTO update(BookDTO book) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            updateBookValidator.validate(modelMapper.map(book, Book.class), em);

            List<Author> authors = authorRepository.getByFullName(book.getAuthor().getName(), book.getAuthor().getLastName(), em);

            if (authors == null || authors.isEmpty()) {
                book.getAuthor().setId(null);
                authorRepository.save(book.getAuthor(), em);
            } else {
                book.getAuthor().setId(authors.get(0).getId());
            }
            bookRepository.update(modelMapper.map(book, Book.class), em);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            throw e;

        } finally {
            em.close();

        }

        return book;

    }

    public BookDTO getById(Long id) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Book dbResult;
        try {
            if (id == null) throw new ValidationException("Id ne sme biti null!");
            else dbResult = bookRepository.findById(id, em);
            em.getTransaction().commit();
            return modelMapper.map(dbResult, BookDTO.class);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }
}
