package rs.ac.bg.fon.libraryback.repository.refactor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.Librarian;
@Repository
public interface UserRepository extends JpaRepository<Librarian, Long> {
    @Query("select m from Librarian m where m.username LIKE ?1 and m.password LIKE ?2")
    Librarian login(String username, String password);

    Librarian findByUsername(String username);
}
