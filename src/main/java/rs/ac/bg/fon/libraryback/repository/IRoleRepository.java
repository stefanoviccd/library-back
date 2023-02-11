package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.Role;
@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    public Role findByName(String name);
}
