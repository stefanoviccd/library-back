package rs.ac.bg.fon.libraryback.repository;


import rs.ac.bg.fon.libraryback.model.LibraryMember;

import java.util.List;

public interface LibraryMemberRepository {
    List<LibraryMember> getAll();

    List<LibraryMember> getByValue(String value);

    List<LibraryMember> getByCardNumber(String cardNumber);

    void delete(Long id);

    LibraryMember getById(Long id);

    LibraryMember update(LibraryMember member);

    List<LibraryMember> findByFullNameAndContact(String firstName, String lastName, String contact);

    LibraryMember save(LibraryMember member);

    LibraryMember getByExactCardNumber(String cardNumber);
}
