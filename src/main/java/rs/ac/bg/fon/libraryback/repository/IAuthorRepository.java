package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.libraryback.model.Author;

public interface IAuthorRepository extends JpaRepository<Author, Long> {
    Author findByNameAndLastName(String authorsName, String authorsLastName);
}
