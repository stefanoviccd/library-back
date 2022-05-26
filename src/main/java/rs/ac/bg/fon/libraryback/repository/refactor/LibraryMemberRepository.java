package rs.ac.bg.fon.libraryback.repository.refactor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.LibraryMember;

import java.util.List;

@Repository
public interface LibraryMemberRepository extends JpaRepository<LibraryMember, Long> {
    @Query("select m from LibraryMember m where m.firstName LIKE ?1 or  m.lastName LIKE ?1 or m.contact LIKE ?1 or m.membershipCard.cardNumber LIKE ?1")
    List<LibraryMember> getByValue(String value);
@Query("select m from LibraryMember m where  m.membershipCard.cardNumber LIKE ?1")
    List<LibraryMember> getByCardNumber(String cardNumber);

    List<LibraryMember> findByFirstNameAndLastNameAndContact(String firstName, String lastName, String contact);

   // LibraryMember getByExactCardNumber(String cardNumber);
}
