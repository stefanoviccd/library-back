package rs.ac.bg.fon.libraryback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.Statistics;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.refactor.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.refactor.BookRepository;
import rs.ac.bg.fon.libraryback.repository.refactor.LibraryMemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Service
public class StatisticsService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryMemberRepository userRepository;
    @Autowired
    private BookRentRepository rentRepository;

    public StatisticsService() {
    }

    public Statistics getStatistics() {
        int bookCount = 0;
        int titleCount = 0;
        int rentedBooksCount = 0;
        int userCount = 0;
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            bookCount = getBookCount();
            titleCount = getTitleCount();
            rentedBooksCount = getRentedBooksCount();
            userCount = getUserCount();
            return new Statistics(bookCount,titleCount,userCount, rentedBooksCount);
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }

    private int getUserCount() {
        List<LibraryMember> dbUsers=userRepository.findAll();
        return dbUsers.size();
    }

    private int getRentedBooksCount() {
        List<BookRent> dbRents=rentRepository.getCurrentlyActiveRents();
        return dbRents.size();
    }

    private int getTitleCount() {
        return bookRepository.getTitleCount();
    }

    private int getBookCount() {
        List<Book> dbBooks = bookRepository.findAll();
        return dbBooks.size();
    }
}
