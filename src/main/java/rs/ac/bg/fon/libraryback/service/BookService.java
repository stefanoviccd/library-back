package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.repository.AuthorRepository;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.impl.AuthorRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.BookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.AddBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.DeleteBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateBookValidator;

import javax.persistence.EntityManager;
import java.util.List;

public class BookService {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private BookValidator deleteValidator;
    private BookValidator addBookValidator;
    private BookValidator updateBookValidator;
    private BookRentRepository rentRepository;

    public BookService() {
        bookRepository = new BookRepositoryImpl();
        authorRepository = new AuthorRepositoryImpl();
        deleteValidator = new DeleteBookValidator();
        addBookValidator = new AddBookValidator();
        updateBookValidator = new UpdateBookValidator();
        rentRepository=new BookRentRepositoryImpl();
    }

    public List<Book> getAllBooks() {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<Book> dbResult = bookRepository.getAll();
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }

    public void deleteBook(Long id) throws ValidationException {
        if (id == null)
            throw new ValidationException("Knjiga za brisanje ne sme imati id null!");
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            checkIfBookIsRented(id);
            Author dbAuthor = getBookAuthor(id);
            List<BookRent> bookRents=rentRepository.getByBook(id);
            for (BookRent rent: bookRents
                 ) {
                em.remove(rent);

            }
            bookRepository.delete(id);

            //TODO delete author if no his books are presented
            if (!areAuthorsBooksPresentedInDatabase(dbAuthor))
                authorRepository.delete(dbAuthor);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

    }

    private boolean areAuthorsBooksPresentedInDatabase(Author dbAuthor) {
        List<Book> authorsBook = bookRepository.getByAuthor(dbAuthor);
        return !authorsBook.isEmpty();
    }

    private Author getBookAuthor(Long id) {
        return bookRepository.findById(id).getAuthor();
    }

    private void checkIfBookIsRented(Long id) throws ValidationException {
        deleteValidator.validate(id);
    }

    public Book save(Book book) throws ValidationException {
        if (book == null)
            throw new ValidationException("Knjiga za čuvanje je null!");
        if (book.getAuthor() == null) {
            throw new ValidationException("Knjiga za čuvanje nema autora!");

        }
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            addBookValidator.validate(book);

            List<Author> authors = authorRepository.getByFullName(book.getAuthor().getName(), book.getAuthor().getLastName());

            if (authors.isEmpty()) {
                authorRepository.save(book.getAuthor());
            } else {
                book.setAuthor(authors.get(0));
            }
            bookRepository.save(book);
            em.getTransaction().commit();
            return book;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

    }

    public List<Book> getByValue(String value) {

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<Book> dbResult;
        try {
            if (value == null || value.isEmpty())
                dbResult = bookRepository.getAll();
            else
                dbResult = bookRepository.getByValue(value);
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

    }

    public Book update(Book book) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        if (book == null) {
            throw new ValidationException("Knjiga za izmenu je null!");
        }
        if (book.getId() == null)
            throw new ValidationException("Knjiga za izmenu ima id null!");

        em.getTransaction().begin();
        try {
            updateBookValidator.validate(book);

            List<Author> authors= authorRepository.getByFullName(book.getAuthor().getName(), book.getAuthor().getLastName());

            if(authors==null || authors.isEmpty()){
                book.getAuthor().setId(null);
                authorRepository.save(book.getAuthor());
            }
            else{
                book.getAuthor().setId(authors.get(0).getId());
            }
            bookRepository.update(book);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

        return book;

    }

    public Book getById(Long id) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Book dbResult;
        try {
            if (id == null)
                throw new ValidationException("Id ne sme biti null!");

            else
                dbResult = bookRepository.findById(id);
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }
    }
}
