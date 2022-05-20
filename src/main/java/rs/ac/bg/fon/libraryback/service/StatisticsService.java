package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.Statistics;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import javax.persistence.EntityManager;
import java.util.List;

public class StatisticsService {
    private BookRepository bookRepository;
    private LibraryMemberRepository userRepository;
    private BookRentRepository rentRepository;

    public StatisticsService() {
        bookRepository = new BookRepositoryImpl();
        userRepository = new LibraryMemberRepositoryImpl();
        rentRepository = new BookRentRepositoryImpl();
    }

    public Statistics getStatistics() {
        int bookCount = 0;
        int titleCount = 0;
        int rentedBooksCount = 0;
        int userCount = 0;
     //   Statistics s = new Statistics(2, 5, 32, 4);
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        LibraryMember dbResult;
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
        List<LibraryMember> dbUsers=userRepository.getAll();
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
        List<Book> dbBooks = bookRepository.getAll();
        return dbBooks.size();
    }
}
