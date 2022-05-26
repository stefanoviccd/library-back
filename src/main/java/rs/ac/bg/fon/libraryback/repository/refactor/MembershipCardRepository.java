package rs.ac.bg.fon.libraryback.repository.refactor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.MembershipCard;

import java.util.List;

@Repository
public interface MembershipCardRepository extends JpaRepository<MembershipCard, Long> {
    List<MembershipCard> getByCardNumber(String cardNumber);

}
