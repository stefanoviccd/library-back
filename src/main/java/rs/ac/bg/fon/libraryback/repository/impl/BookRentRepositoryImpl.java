package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class BookRentRepositoryImpl implements BookRentRepository {
    @Override
    public List<BookRent> getByUser(Long memberId) {
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        List<BookRent> rents = em.createQuery("select m from BookRent m where m.byMember.id = :id order by m.returnDate asc, m.rentDate desc").setParameter("id", memberId
        ).getResultList();
        return rents;
    }

    @Override
    public BookRent save(BookRent bookRent) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Book b=bookRent.getBook();
        em.persist(bookRent);
        b.setCurrentlyRented(true);
        em.merge(b);
        return bookRent;

    }

    @Override
    public void update(BookRent bookRent) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.find(BookRent.class, bookRent.getId());
        em.merge(bookRent);
    }

    @Override
    public List<BookRent> getCurrentlyActiveRents() {
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        List<BookRent> rents = em.createQuery("select m from BookRent m where m.returnDate is null ")
        .getResultList();
        return rents;
    }

    @Override
    public List<BookRent> getByBook(Long id) {
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        List<BookRent> rents = em.createQuery("select m from BookRent m where m.book.id = :id").setParameter("id", id
        ).getResultList();
        return rents;

    }
}
