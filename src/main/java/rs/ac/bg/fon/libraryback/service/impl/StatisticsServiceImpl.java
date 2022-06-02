package rs.ac.bg.fon.libraryback.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.StatisticsDTO;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.Statistics;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.service.StatisticsService;


import javax.persistence.EntityManager;
import java.util.List;
@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryMemberRepository userRepository;
    @Autowired
    private BookRentRepository rentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public StatisticsServiceImpl() {
    }

    public StatisticsDTO getStatistics() {
        int bookCount = 0;
        int titleCount = 0;
        int rentedBooksCount = 0;
        int userCount = 0;
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            bookCount = getBookCount(em);
            titleCount = getTitleCount(em);
            rentedBooksCount = getRentedBooksCount(em);
            userCount = getUserCount(em);
            return modelMapper.map(new Statistics(bookCount,titleCount,userCount, rentedBooksCount), StatisticsDTO.class);
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }


    }

    private int getUserCount(EntityManager em) {
        List<LibraryMember> dbUsers=userRepository.getAll(em);
        return dbUsers.size();
    }

    private int getRentedBooksCount(EntityManager em) {
        List<BookRent> dbRents=rentRepository.getCurrentlyActiveRents(em);
        return dbRents.size();
    }

    private int getTitleCount(EntityManager em) {
        return bookRepository.getTitleCount(em);
    }

    private int getBookCount(EntityManager em) {
        List<Book> dbBooks = bookRepository.getAll(em);
        return dbBooks.size();
    }
}
