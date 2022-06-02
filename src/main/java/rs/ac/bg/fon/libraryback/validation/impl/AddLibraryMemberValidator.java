package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.MembershipCardRepository;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.MembershipCardRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import javax.persistence.EntityManager;
import java.util.List;
public class AddLibraryMemberValidator implements LibraryMemberValidator {
    private LibraryMemberRepository memberRepository;
    private MembershipCardRepository membershipCardRepository;
    public AddLibraryMemberValidator(){
        memberRepository=new LibraryMemberRepositoryImpl();
        membershipCardRepository=new MembershipCardRepositoryImpl();
    }
    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        LibraryMember member=(LibraryMember) o;
        if (member == null) throw new ValidationException("Član za čuvanje je null!");
        if (member.getMembershipCard() == null) {
            throw new ValidationException("Član za čuvanje nema člansku kartu!");

        }
        if (member.getMembershipCard().getCardNumber() == null || member.getMembershipCard().getCardNumber().isEmpty()) {
            throw new ValidationException("Mora se obezbediti broj članske karte!");

        }
        if(userWithSameNameAndContactExists(member, em))
            throw new ValidationException("Korisnik sa istim podacima postoji u bazi.");
        if(isMembershipCardNumberTaken(member.getMembershipCard(), em))
            throw new ValidationException("Broj članske karte zauzet.");

    }

    private boolean isMembershipCardNumberTaken(MembershipCard membershipCard, EntityManager em) {
        List<MembershipCard> dbCards=membershipCardRepository.getByCardNumber(membershipCard.getCardNumber(), em);
        return !dbCards.isEmpty();
    }

    private boolean userWithSameNameAndContactExists(LibraryMember member, EntityManager em) {
        List<LibraryMember> dbMembers=memberRepository.findByFullNameAndContact(member.getFirstName(), member.getLastName(), member.getContact(), em);
        return !dbMembers.isEmpty();
    }
}
