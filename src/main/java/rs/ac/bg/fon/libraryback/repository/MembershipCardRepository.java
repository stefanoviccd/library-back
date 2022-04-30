package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.model.MembershipCard;

import java.util.List;

public interface MembershipCardRepository {
    List<MembershipCard> getByCardNumber(String cardNumber);

    MembershipCard save(MembershipCard membershipCard);

    void delete(MembershipCard dbMembershipCard);

    MembershipCard getById(Long id);

    MembershipCard update(MembershipCard membershipCard);
}
