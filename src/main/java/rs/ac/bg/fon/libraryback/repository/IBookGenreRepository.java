package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.BookGenre;

@Repository
public interface IBookGenreRepository extends JpaRepository<BookGenre, Long> {
    BookGenre findByName(String genre);
}
