package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.repository.MembershipCardRepository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
@Repository
public class MembershipCardRepositoryImpl implements MembershipCardRepository {
    @Override
    public List<MembershipCard> getByCardNumber(String cardNumber) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        String searchingParameter=cardNumber;
        List<MembershipCard> cards = em.createQuery("select m from MembershipCard m where m.cardNumber LIKE :value").setParameter("value", searchingParameter)
                .getResultList();
        return cards;

    }

    @Override
    public MembershipCard save(MembershipCard membershipCard) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.persist(membershipCard);
        return membershipCard;
    }

    @Override
    public void delete(MembershipCard dbMembershipCard) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        MembershipCard dbCard = em.find(MembershipCard.class, dbMembershipCard.getId());
        em.remove(dbCard);
    }

    @Override
    public MembershipCard getById(Long id) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        MembershipCard membershipCard = em.find(MembershipCard.class, id);
        return membershipCard;
    }

    @Override
    public MembershipCard update(MembershipCard membershipCard) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        MembershipCard dbCard=em.find(MembershipCard.class, membershipCard.getId());
        if(dbCard.getCardNumber()!=membershipCard.getCardNumber()){
            membershipCard.setIssueDate(LocalDate.now());
            membershipCard.setExpiryDate(LocalDate.now().plusYears(2));
        }
        em.merge(membershipCard);
        return membershipCard;
    }
}
