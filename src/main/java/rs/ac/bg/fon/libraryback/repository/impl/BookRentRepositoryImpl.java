package rs.ac.bg.fon.libraryback.repository.impl;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class BookRentRepositoryImpl implements BookRentRepository {
    @Override
    public List<BookRent> getByUser(Long memberId) {
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        List<BookRent> rents = em.createQuery("select m from BookRent m where m.byMember.id = :id").setParameter("id", memberId
        ).getResultList();
        return rents;
    }

    @Override
    public BookRent save(BookRent bookRent) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.persist(bookRent);
        return bookRent;

    }

    @Override
    public void update(BookRent bookRent) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.find(BookRent.class, bookRent.getId());
        em.merge(bookRent);
    }
}
