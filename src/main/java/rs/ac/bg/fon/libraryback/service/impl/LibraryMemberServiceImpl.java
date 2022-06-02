package rs.ac.bg.fon.libraryback.service.impl;


import org.apache.tomcat.jni.Library;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.LibraryMemberDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.MembershipCardRepository;
import rs.ac.bg.fon.libraryback.service.LibraryMemberService;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.AddLibraryMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.DeleteMemberValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateMemberValidator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class LibraryMemberServiceImpl implements LibraryMemberService {
    @Autowired
    private LibraryMemberRepository memberRepository;
    @Autowired
    private MembershipCardRepository membershipCardRepository;
    @Autowired
    private BookRentRepository rentRepository;
    private LibraryMemberValidator addMemberValidator;
    private LibraryMemberValidator deleteMemberValidator;
    private LibraryMemberValidator updateMemberValidator;
    @Autowired
    private ModelMapper modelMapper;


    public LibraryMemberServiceImpl() {
        addMemberValidator = new AddLibraryMemberValidator();
        deleteMemberValidator = new DeleteMemberValidator();
        updateMemberValidator = new UpdateMemberValidator();
    }

    public List<LibraryMemberDTO> getAllMembers() {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {

            List<LibraryMemberDTO> dbResult = memberRepository.getAll(em).stream().map(r -> modelMapper.map(r, LibraryMemberDTO.class)).collect(Collectors.toList());;
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }

    }

    public List<LibraryMemberDTO> getByValue(String value) {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        List<LibraryMemberDTO> dbResult;
        try {
            if (value == null || value.isEmpty()) dbResult = memberRepository.getAll(em).stream().map(r -> modelMapper.map(r, LibraryMemberDTO.class)).collect(Collectors.toList());

            else dbResult = memberRepository.getByValue(value, em).stream().map(r -> modelMapper.map(r, LibraryMemberDTO.class)).collect(Collectors.toList());
            em.getTransaction().commit();
            return dbResult;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }

    }

    public void deleteMember(Long id) throws ValidationException {
        if (id == null) throw new ValidationException("Član za brisanje ima id null!");
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            // TODO: implement
            deleteMemberValidator.validate(id, em);
            MembershipCard dbMembershipCard = modelMapper.map(getUserCard(id, em), MembershipCard.class);
            membershipCardRepository.delete(dbMembershipCard, em);
            List<BookRent> bookRents = rentRepository.getByUser(id, em);
            for (BookRent rent : bookRents) {
                em.remove(rent);

            }
            memberRepository.delete(id, em);

            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }


    }

    private MembershipCard getUserCard(Long id, EntityManager em) {
        LibraryMember member = memberRepository.getById(id, em);
        return member.getMembershipCard();
    }

    public LibraryMemberDTO update(LibraryMemberDTO member) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            updateMemberValidator.validate(modelMapper.map(member, LibraryMember.class), em);
            membershipCardRepository.update(member.getMembershipCard(), em);
            memberRepository.update(modelMapper.map(member, LibraryMember.class), em);
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            throw e;

        } finally {
            em.close();

        }

        return member;

    }

    public LibraryMemberDTO save(LibraryMemberDTO member) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            addMemberValidator.validate(modelMapper.map(member, LibraryMember.class), em);
            List<MembershipCard> dbCards = membershipCardRepository.getByCardNumber(member.getMembershipCard().getCardNumber(), em);
            if (!dbCards.isEmpty()) {
                throw new ValidationException("Broj članske karte već postoji!");
            }
            member.getMembershipCard().setIssueDate(LocalDate.now());
            member.getMembershipCard().setExpiryDate(LocalDate.now().plusYears(2));
            membershipCardRepository.save(member.getMembershipCard(), em);
            memberRepository.save(modelMapper.map(member, LibraryMember.class), em);
            em.getTransaction().commit();
            return member;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }
    }

    public String generateCardNumber() {
        Random randomGenerator = new Random();
        String cardNumber = "";
        for (int i = 0; i < 15; i++)
            cardNumber += randomGenerator.nextInt(10);
        return cardNumber;
    }

    public LibraryMemberDTO getById(Long value) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        LibraryMember dbResult;
        try {
            if (value == null) throw new ValidationException("Mora postojati id za pretragu!");
            else em.getTransaction().begin();
            dbResult = memberRepository.getById(value, em);
            em.getTransaction().commit();
            return modelMapper.map(dbResult, LibraryMemberDTO.class);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }


    }

    public LibraryMemberDTO getByExactCardNumber(String cardNumber) throws ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        LibraryMember dbResult;
        try {
            if (cardNumber == null || cardNumber.isEmpty())
                throw new ValidationException("Mora postojati broj članske karte za pretragu!");
            else dbResult = memberRepository.getByExactCardNumber(cardNumber, em);
            em.getTransaction().commit();
            return modelMapper.map(dbResult, LibraryMemberDTO.class);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();

        }
    }
}
