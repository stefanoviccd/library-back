package rs.ac.bg.fon.libraryback.repository.impl;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import javax.persistence.EntityManager;
import java.util.List;

public class LibraryMemberRepositoryImpl implements LibraryMemberRepository {
    @Override
    public List<LibraryMember> getAll() {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<LibraryMember> dbResult = em.createQuery("select m from LibraryMember m")
                .getResultList();
        return dbResult;
    }

    @Override
    public List<LibraryMember> getByValue(String value) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        String searchingParameter="%"+value+"%";
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where m.firstName LIKE :value or  m.lastName LIKE :value or m.contact LIKE :value or m.membershipCard.cardNumber LIKE :value").setParameter("value", searchingParameter)
                .getResultList();
        return members;
    }

    @Override
    public List<LibraryMember> getByCardNumber(String cardNumber) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where  m.membershipCard.cardNumber LIKE :value").setParameter("value", cardNumber)
                .getResultList();
        return members;
    }

    @Override
    public void delete(Long id) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        LibraryMember dbMember = em.find(LibraryMember.class, id);
        em.remove(dbMember);
    }

    @Override
    public LibraryMember getById(Long id) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        LibraryMember dbMember = em.find(LibraryMember.class, id);
        return dbMember;
    }

    @Override
    public LibraryMember update(LibraryMember member) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        LibraryMember dbMember=em.find(LibraryMember.class, member.getId());
        em.merge(member);
        return dbMember;

    }

    @Override
    public List<LibraryMember> findByFullNameAndContact(String firstName, String lastName, String contact) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        List<LibraryMember> members = em.createQuery("select m from LibraryMember m where  m.firstName LIKE :firstName and m.lastName= :lastName and m.contact = :contact ").setParameter("firstName", firstName)
                .setParameter("lastName", lastName).setParameter("contact", contact)
                .getResultList();
        return members;
    }

    @Override
    public LibraryMember save(LibraryMember member) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.persist(member);
        return member;

    }

    @Override
    public LibraryMember getByExactCardNumber(String cardNumber) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        LibraryMember member = (LibraryMember) em.createQuery("select m from LibraryMember m where  m.membershipCard.cardNumber LIKE :value").setParameter("value", cardNumber)
                .getSingleResult();
        return member;
    }
}
