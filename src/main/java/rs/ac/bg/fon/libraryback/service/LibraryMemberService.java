package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.MembershipCardRepository;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.MembershipCardRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.AddLibraryMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.DeleteMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateMemberValidator;

import javax.persistence.EntityManager;
import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.List;

public class LibraryMemberService {
    private LibraryMemberRepository memberRepository;
    private MembershipCardRepository membershipCardRepository;
    private LibraryMemberValidator addMemberValidator;
    private LibraryMemberValidator deleteMemberValidator;
    private LibraryMemberValidator updateMemberValidator;

    public LibraryMemberService() {
        memberRepository = new LibraryMemberRepositoryImpl();
        membershipCardRepository=new MembershipCardRepositoryImpl();
        addMemberValidator=new AddLibraryMemberValidator();
        deleteMemberValidator=new DeleteMemberValidator();
        updateMemberValidator=new UpdateMemberValidator();
    }

    public List<LibraryMember> getAllMembers() {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<LibraryMember> dbResult = memberRepository.getAll();
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

    }

    public List<LibraryMember> getByValue(String value) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<LibraryMember> dbResult;
        try {
            if (value == null || value.isEmpty())
                dbResult = memberRepository.getAll();
            else
                dbResult = memberRepository.getByValue(value);
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

    }

    public void deleteMember(Long id) throws ValidationException  {
        if (id == null)
            throw new ValidationException("Member to be deleted cannot have id null!");
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            // TODO: implement
            deleteMemberValidator.validate(id);
            MembershipCard dbMembershipCard = getUserCard(id);
            membershipCardRepository.delete(dbMembershipCard);
            memberRepository.delete(id);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }

    private MembershipCard getUserCard(Long id) {
        LibraryMember member=memberRepository.getById(id);
        return member.getMembershipCard();
    }

    public LibraryMember update(LibraryMember member) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        if (member == null) {
            throw new ValidationException("Library member to be updated is null!");
        }
        if (member.getId() == null)
            throw new ValidationException("Library member to be updated has id null!");

        em.getTransaction().begin();
        try {
            //TODO: implement
            updateMemberValidator.validate(member);
            membershipCardRepository.update(member.getMembershipCard());
            memberRepository.update(member);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            e.printStackTrace();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }

        return member;

    }

    public LibraryMember save(LibraryMember member) throws ValidationException {
        if (member == null)
            throw new ValidationException("Library member to be saved is null!");
        if (member.getMembershipCard() == null) {
            throw new ValidationException("Library member to be saved has membership card null!");

        }
        if (member.getMembershipCard().getCardNumber() == null || member.getMembershipCard().getCardNumber().isEmpty() ) {
            throw new ValidationException("Membership card number must be prvided for saving member!");

        }
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            //TODO: implement
            addMemberValidator.validate(member);
            List<MembershipCard> dbCards = membershipCardRepository.getByCardNumber(member.getMembershipCard().getCardNumber());
            if (!dbCards.isEmpty()) {
               throw  new ValidationException("Membership card number is already exist!");
            }
            member.getMembershipCard().setIssueDate(LocalDate.now());
            member.getMembershipCard().setExpiryDate(LocalDate.now().plusYears(2));
            membershipCardRepository.save(member.getMembershipCard());
            memberRepository.save(member);
            em.getTransaction().commit();
            return member;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }
    }

    public List<LibraryMember> getByCardNumber(String cardNumber) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<LibraryMember> dbResult;
        try {
            if (cardNumber == null || cardNumber.isEmpty())
                throw new ValidationException("Card number must be provided for finding member by it!");
            else
                dbResult = memberRepository.getByCardNumber(cardNumber);
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive())
                em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }
    }
}
