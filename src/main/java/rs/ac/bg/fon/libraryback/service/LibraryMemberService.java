package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dto.LibraryMemberDTO;
import rs.ac.bg.fon.libraryback.dto.MembershipCardDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.MembershipCard;

import javax.persistence.EntityManager;
import java.util.List;

public interface LibraryMemberService {
    List<LibraryMemberDTO> getAllMembers();
    List<LibraryMemberDTO> getByValue(String value);
    void deleteMember(Long id) throws ValidationException;
    LibraryMemberDTO update(LibraryMemberDTO member) throws ValidationException;
    LibraryMemberDTO save(LibraryMemberDTO member) throws ValidationException;
    String generateCardNumber();
    LibraryMemberDTO getById(Long value) throws ValidationException;
    LibraryMemberDTO getByExactCardNumber(String cardNumber) throws ValidationException;


}
