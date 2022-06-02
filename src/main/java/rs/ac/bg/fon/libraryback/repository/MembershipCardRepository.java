package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.model.MembershipCard;

import javax.persistence.EntityManager;
import java.util.List;

public interface MembershipCardRepository {
    List<MembershipCard> getByCardNumber(String cardNumber, EntityManager em);

    MembershipCard save(MembershipCard membershipCard, EntityManager em);

    void delete(MembershipCard dbMembershipCard, EntityManager em);

    MembershipCard getById(Long id, EntityManager em);

    MembershipCard update(MembershipCard membershipCard, EntityManager em);
}
