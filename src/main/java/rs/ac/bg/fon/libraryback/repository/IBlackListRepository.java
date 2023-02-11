package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.Blacklist;
@Repository

public interface IBlackListRepository extends JpaRepository<Blacklist, Long> {
    Blacklist getByToken(String token);
}
