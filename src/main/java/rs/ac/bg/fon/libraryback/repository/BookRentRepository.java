package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.model.BookRent;

import javax.persistence.EntityManager;
import java.util.List;

public interface BookRentRepository {
    List<BookRent> getByUser(Long userId, EntityManager em);

    BookRent save(BookRent bookRent, EntityManager em);

    void update(BookRent bookRent, EntityManager em);

    List<BookRent> getCurrentlyActiveRents(EntityManager em);

    List<BookRent> getByBook(Long id, EntityManager em);
}
