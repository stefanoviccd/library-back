package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.RentsValidator;
import rs.ac.bg.fon.libraryback.validation.impl.RentValidator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class BookRentService {
    private BookRentRepository rentRepository;
    private RentsValidator rentsValidator;
    private BookRepository bookRepository;

    public BookRentService() {
        rentRepository = new BookRentRepositoryImpl();
        bookRepository=new BookRepositoryImpl();
        rentsValidator=new RentValidator();
    }

    public List<BookRent> getUserRents(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Id korisnika mora postojati!");
        }
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<BookRent> dbRents = rentRepository.getByUser(id);
            em.getTransaction().commit();
            return dbRents;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }

    public void rentBook(LibraryMember member, Book book) throws ValidationException {
        if (member == null)
            throw new ValidationException("Pri iznajmljivanju knjige korisnik mora postojati!");
        if (book == null)
            throw new ValidationException("Pri iznajmljivanju knjige knjiga mora postojati!");
        if(member.getId()==null)
            throw new ValidationException("Član ne sme imati id null!");
        if(book.getId()==null)
            throw new ValidationException("Knjiga ne sme imati id null!");

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            rentsValidator.validate(member, book);
            BookRent bookRent=new BookRent();
            bookRent.setBook(book);
            bookRent.setByMember(member);
            bookRent.setRentDate(LocalDate.now());
            bookRent.setReturnDate(null);
            rentRepository.save(bookRent);
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

    public void restoreBook(BookRent rent) throws ValidationException {
        if (rent == null)
            throw new ValidationException("Iznajmljivanje ne sme biti null!");
        if (rent.getId() == null)
            throw new ValidationException("Id iznajmljivanja ne sme biti null!");

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            rent.setReturnDate(LocalDate.now());
            rentRepository.update(rent);
            rent.getBook().setCurrentlyRented(false);
            bookRepository.update(rent.getBook());

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
}
