package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.BookRent;

import java.util.List;

@Repository
public interface IBookRentRepository extends JpaRepository<BookRent, Long> {
    @Query(value = "SELECT * FROM book_rent WHERE USER=?1", nativeQuery = true)
    List<BookRent> findByUser(Long id, Pageable p);
    @Query(value = "SELECT * FROM book_rent WHERE USER=?1", nativeQuery = true)
    List<BookRent> findByUser(Long id);

    @Query(value = "SELECT * FROM book_rent WHERE USER=?1 AND return_date IS NULL", nativeQuery = true)
    List<BookRent> getActiveByUser(Long id);

    @Query(value = "SELECT * FROM book_rent WHERE USER=?1 AND book=?2 AND return_date IS NULL", nativeQuery = true)
    BookRent findByUserAndBook(Long userId, Long bookId);
}
