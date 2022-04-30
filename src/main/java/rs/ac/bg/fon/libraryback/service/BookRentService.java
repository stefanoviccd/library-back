package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.RentsValidator;
import rs.ac.bg.fon.libraryback.validation.impl.RentValidator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class BookRentService {
    private BookRentRepository rentRepository;
    private RentsValidator rentsValidator;

    public BookRentService() {
        rentRepository = new BookRentRepositoryImpl();
        rentsValidator=new RentValidator();
    }

    public List<BookRent> getUserRents(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Id for getting user rents must be provided!");
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
            throw new ValidationException("Member for book rent cannot be null!");
        if (book == null)
            throw new ValidationException("Book for book rent cannot be null!");
        if(member.getId()==null)
            throw new ValidationException("Member for book rent has id null!");
        if(book.getId()==null)
            throw new ValidationException("Book for book rent has id null!");

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
            throw new ValidationException("Rent for book return cannot be null!");
        if (rent.getId() == null)
            throw new ValidationException("Rent for book return has id null!");

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            rent.setRentDate(LocalDate.now());
            rentRepository.update(rent);
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
