package rs.ac.bg.fon.libraryback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.bg.fon.libraryback.model.MembershipCard;

import java.lang.reflect.Member;
import java.util.Optional;

public interface IMembershipCardRepository extends JpaRepository<MembershipCard, Long> {
    Optional<MembershipCard> findByCardNumber(String cardNumber);
}
