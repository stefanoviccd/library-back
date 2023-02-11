package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.bg.fon.libraryback.model.User;

import java.util.List;

public interface IUserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    @Query(value = "SELECT * FROM USER b WHERE b.first_name LIKE ?1 OR b.last_name LIKE ?1 OR b.card_id= (\n" +
            "SELECT id FROM membership_card WHERE card_number LIKE ?1)", nativeQuery = true)

    List<User> findByValue(String searchValue, Pageable p);
}
