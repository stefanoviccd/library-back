package rs.ac.bg.fon.libraryback.repository.refactor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.BookRent;

import java.util.List;
@Repository
public interface BookRentRepository extends JpaRepository<BookRent, Long> {
    @Query("select m from BookRent m where m.byMember.id = ?1 order by m.returnDate asc, m.rentDate desc")
    List<BookRent> getByUser(Long userId);

    @Query("select m from BookRent m where m.returnDate is null ")
    List<BookRent> getCurrentlyActiveRents();

    @Query("select m from BookRent m where m.book.id = ?1")
    List<BookRent> getByBook(Long id);
}
