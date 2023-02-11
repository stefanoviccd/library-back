package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;
@Repository
public interface IFileRepository extends JpaRepository<ProfilePicture, Long> {
}
