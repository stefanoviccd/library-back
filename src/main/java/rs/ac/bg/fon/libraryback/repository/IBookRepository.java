package rs.ac.bg.fon.libraryback.repository;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.result.Row;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.bg.fon.libraryback.dto.BookSummaryDTO;
import rs.ac.bg.fon.libraryback.model.Book;

import javax.persistence.Table;
import javax.sql.RowSet;
import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.util.List;

public interface IBookRepository extends JpaRepository<Book,Long> {
    Book findByISBN(String isbn);
    @Query(value = "SELECT COUNT(*) as count, title, author_id, genre_id FROM book GROUP BY title, author_id, genre_id", nativeQuery = true)
    List<?>  getSummary(Pageable p);
    @Query(value = "select * from book b where b.title= ?1 and b.author_id= ?2 and is_currently_rented=true", nativeQuery = true)
    List<Book> findCurrentlyRentedCount(String title, Long authorId);
    @Query(value = "SELECT * FROM book where title like ?1 or isbn like ?1", nativeQuery = true)
    List<Book> findByValue(String value,Pageable p);
}
