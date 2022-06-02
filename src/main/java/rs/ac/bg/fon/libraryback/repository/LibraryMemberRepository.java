package rs.ac.bg.fon.libraryback.repository;


import rs.ac.bg.fon.libraryback.model.LibraryMember;

import javax.persistence.EntityManager;
import java.util.List;

public interface LibraryMemberRepository {
    List<LibraryMember> getAll(EntityManager em);

    List<LibraryMember> getByValue(String value,EntityManager em);

    List<LibraryMember> getByCardNumber(String cardNumber,EntityManager em);

    void delete(Long id, EntityManager em);

    LibraryMember getById(Long id, EntityManager em);

    LibraryMember update(LibraryMember member, EntityManager em);

    List<LibraryMember> findByFullNameAndContact(String firstName, String lastName, String contact, EntityManager em);

    LibraryMember save(LibraryMember member, EntityManager em);

    LibraryMember getByExactCardNumber(String cardNumber, EntityManager em);
}
