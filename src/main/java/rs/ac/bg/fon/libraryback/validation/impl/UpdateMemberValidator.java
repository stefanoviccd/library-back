package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import javax.persistence.EntityManager;
import java.util.List;

public class UpdateMemberValidator implements LibraryMemberValidator {
    private LibraryMemberRepository memberRepository;
    public UpdateMemberValidator(){
        memberRepository=new LibraryMemberRepositoryImpl();
    }
    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        LibraryMember member=(LibraryMember) o;
        if (member == null) {
            throw new ValidationException("Član za izmenu je null!");
        }
        if (member.getId() == null) throw new ValidationException("Član za izmenu ima id null!");

        if(userWithSameNameAndContactExists(member, em))
            throw new ValidationException("Korisnik sa identičnim podacima postoji u sistemu.");
        if(isMembershipCardNumberTaken(member, em))
            throw new ValidationException("Broj članske karte zauzet.");

    }

    private boolean isMembershipCardNumberTaken(LibraryMember member, EntityManager em) {
        List<LibraryMember> dbMembers=memberRepository.getByCardNumber(member.getMembershipCard().getCardNumber(), em);
        return !dbMembers.isEmpty() && dbMembers.get(0).getId()!=member.getId();
    }


    private boolean userWithSameNameAndContactExists(LibraryMember member,  EntityManager em) {
        List<LibraryMember> dbMembers=memberRepository.findByFullNameAndContact(member.getFirstName(), member.getLastName(), member.getContact(), em);
        return !dbMembers.isEmpty() && dbMembers.get(0).getId()!=member.getId();
    }
}
