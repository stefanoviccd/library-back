package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.MembershipCardRepository;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.MembershipCardRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import java.util.List;

public class AddLibraryMemberValidator implements LibraryMemberValidator {
    private LibraryMemberRepository memberRepository;
    private MembershipCardRepository membershipCardRepository;
    public AddLibraryMemberValidator(){
        memberRepository=new LibraryMemberRepositoryImpl();
        membershipCardRepository=new MembershipCardRepositoryImpl();
    }
    @Override
    public void validate(Object o) throws ValidationException {
        LibraryMember member=(LibraryMember) o;
        if(userWithSameNameAndContactExists(member))
            throw new ValidationException("Korisnik sa istim podacima postoji u bazi.");
        if(isMembershipCardNumberTaken(member.getMembershipCard()))
            throw new ValidationException("Broj članske karte zauzet.");

    }

    private boolean isMembershipCardNumberTaken(MembershipCard membershipCard) {
        List<MembershipCard> dbCards=membershipCardRepository.getByCardNumber(membershipCard.getCardNumber());
        return !dbCards.isEmpty();
    }

    private boolean userWithSameNameAndContactExists(LibraryMember member) {
        List<LibraryMember> dbMembers=memberRepository.findByFullNameAndContact(member.getFirstName(), member.getLastName(), member.getContact());
        return !dbMembers.isEmpty();
    }
}
