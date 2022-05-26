package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;

import java.util.List;

public interface AuthorRepository {
    void delete(Author dbAuthor) throws ValidationException;

    List<Author> getByFullName(String name, String lastName);

    void save(Author author) throws ValidationException;
}
