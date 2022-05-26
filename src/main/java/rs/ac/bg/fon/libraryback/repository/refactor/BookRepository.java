package rs.ac.bg.fon.libraryback.repository.refactor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> getByISBN(String isbn);
    @Query("select m from Book m where m.ISBN LIKE ?1 or  m.title LIKE ?1 or m.author.name LIKE ?1 or m.author.lastName LIKE ?1")
    List<Book> getByValue(String value);
    @Query("select distinct count(m.title) from Book m")
    int getTitleCount();
    @Query("select m from Book m where m.author.id= ?1")
    List<Book> getByAuthor(Long authodId);

}
