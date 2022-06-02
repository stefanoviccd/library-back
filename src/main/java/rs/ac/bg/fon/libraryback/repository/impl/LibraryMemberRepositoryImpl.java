package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class LibraryMemberRepositoryImpl implements LibraryMemberRepository {
    @Override
    public List<LibraryMember> getAll(EntityManager em) {
        List<LibraryMember> dbResult = em.createQuery("select m from LibraryMember m")
                .getResultList();
        return dbResult;
    }

    @Override
    public List<LibraryMember> getByValue(String value, EntityManager em) {
        String searchingParameter="%"+value+"%";
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where m.firstName LIKE :value or  m.lastName LIKE :value or m.contact LIKE :value or m.membershipCard.cardNumber LIKE :value").setParameter("value", searchingParameter)
                .getResultList();
        if(value.trim().contains(" ")){
            String firstName=value.trim().split(" ")[0];
            String lastName=value.trim().split(" ")[1]+"%";
            List<LibraryMember> membersByFullName = em.createQuery("select m from LibraryMember m where m.firstName LIKE :firstName and  m.lastName LIKE :lastName").setParameter("firstName", firstName).setParameter("lastName", lastName)
                    .getResultList();
            for (LibraryMember m: membersByFullName
                 ) {
             if(!members.contains(m))
                 members.add(m);
            }
        }

        return members;
    }

    @Override
    public List<LibraryMember> getByCardNumber(String cardNumber, EntityManager em) {
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where  m.membershipCard.cardNumber LIKE :value").setParameter("value", cardNumber)
                .getResultList();
        return members;
    }

    @Override
    public void delete(Long id, EntityManager em) {
        LibraryMember dbMember = em.find(LibraryMember.class, id);
        em.remove(dbMember);
    }

    @Override
    public LibraryMember getById(Long id, EntityManager em) {
        LibraryMember dbMember = em.find(LibraryMember.class, id);
        return dbMember;
    }

    @Override
    public LibraryMember update(LibraryMember member, EntityManager em) {
        LibraryMember dbMember=em.find(LibraryMember.class, member.getId());
        em.merge(member);
        return dbMember;

    }

    @Override
    public List<LibraryMember> findByFullNameAndContact(String firstName, String lastName, String contact, EntityManager em) {
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where  m.firstName LIKE :firstName and m.lastName= :lastName and m.contact = :contact ").setParameter("firstName", firstName)
                .setParameter("lastName", lastName).setParameter("contact", contact)
                .getResultList();
        return members;
    }

    @Override
    public LibraryMember save(LibraryMember member, EntityManager em) {
        em.persist(member);
        return member;

    }

    @Override
    public LibraryMember getByExactCardNumber(String cardNumber, EntityManager em) {
        LibraryMember member = (LibraryMember) em.createQuery("select m from LibraryMember m where  m.membershipCard.cardNumber LIKE :value").setParameter("value", cardNumber)
                .getSingleResult();
        return member;
    }
}
