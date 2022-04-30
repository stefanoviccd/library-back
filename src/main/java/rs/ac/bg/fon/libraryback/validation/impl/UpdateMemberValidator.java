package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import java.util.List;

public class UpdateMemberValidator implements LibraryMemberValidator {
    private LibraryMemberRepository memberRepository;
    public UpdateMemberValidator(){
        memberRepository=new LibraryMemberRepositoryImpl();
    }
    @Override
    public void validate(Object o) throws ValidationException {
        LibraryMember member=(LibraryMember) o;
        if(userWithSameNameAndContactExists(member))
            throw new ValidationException("User with name and contact provided exists in database");
        if(isMembershipCardNumberTaken(member))
            throw new ValidationException("Membership card number is taken");

    }

    private boolean isMembershipCardNumberTaken(LibraryMember member) {
        List<LibraryMember> dbMembers=memberRepository.getByCardNumber(member.getMembershipCard().getCardNumber());
        return !dbMembers.isEmpty() && dbMembers.get(0).getId()!=member.getId();
    }


    private boolean userWithSameNameAndContactExists(LibraryMember member) {
        List<LibraryMember> dbMembers=memberRepository.findByFullNameAndContact(member.getFirstName(), member.getLastName(), member.getContact());
        return !dbMembers.isEmpty() && dbMembers.get(0).getId()!=member.getId();
    }
}
