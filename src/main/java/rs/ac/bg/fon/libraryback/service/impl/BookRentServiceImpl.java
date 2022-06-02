package rs.ac.bg.fon.libraryback.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.dto.LibraryMemberDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.service.BookRentService;
import rs.ac.bg.fon.libraryback.validation.RentsValidator;
import rs.ac.bg.fon.libraryback.validation.impl.RentValidator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRentServiceImpl implements BookRentService {
    @Autowired
    private BookRentRepository rentRepository;

    private RentsValidator rentsValidator;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ModelMapper modelMapper;

    public BookRentServiceImpl() {
        rentsValidator=new RentValidator();
    }

    public List<BookRentDTO> getUserRents(Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Id korisnika mora postojati!");
        }
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<BookRentDTO> dbRents = rentRepository.getByUser(id, em).stream().map(r->modelMapper.map(r, BookRentDTO.class)).collect(Collectors.toList());
            em.getTransaction().commit();
            return dbRents;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }


    }

    public void rentBook(LibraryMember member, Book book) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            rentsValidator.validate(member, book, em);
            BookRent bookRent=new BookRent();
            bookRent.setBook(book);
            bookRent.setByMember(member);
            bookRent.setRentDate(LocalDate.now());
            bookRent.setReturnDate(null);
            rentRepository.save(bookRent, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }



    }

    public void restoreBook(BookRentDTO rent) throws ValidationException {
        if (rent == null)
            throw new ValidationException("Iznajmljivanje ne sme biti null!");
        if (rent.getId() == null)
            throw new ValidationException("Id iznajmljivanja ne sme biti null!");

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            rent.setReturnDate(LocalDate.now());
            rentRepository.update(modelMapper.map(rent, BookRent.class), em);
            rent.getBook().setCurrentlyRented(false);
            bookRepository.update(rent.getBook(), em);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }


    }
}
