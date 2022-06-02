package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.BookRepository;
import rs.ac.bg.fon.libraryback.repository.LibraryMemberRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.BookRepositoryImpl;
import rs.ac.bg.fon.libraryback.repository.impl.LibraryMemberRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.RentsValidator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class RentValidator implements RentsValidator {
    private LibraryMemberRepository memberRepository;
    private BookRepository bookRepository;
    private BookRentRepository rentRepository;
    public RentValidator(){
        memberRepository=new LibraryMemberRepositoryImpl();
        bookRepository=new BookRepositoryImpl();
        rentRepository=new BookRentRepositoryImpl();
    }
    @Override
    public void validate(LibraryMember member, Book book, EntityManager em) throws ValidationException {
        if (member == null)
            throw new ValidationException("Pri iznajmljivanju knjige korisnik mora postojati!");
        if (book == null)
            throw new ValidationException("Pri iznajmljivanju knjige knjiga mora postojati!");
        if(member.getId()==null)
            throw new ValidationException("Član ne sme imati id null!");
        if(book.getId()==null)
            throw new ValidationException("Knjiga ne sme imati id null!");

        if(!isMemberExistsInDatabase(member, em))
            throw new ValidationException("Član ne postoji u sistemu!");
        if(isMemberCardExpiry(member))
            throw new ValidationException("Članska karta je istekla!");
        if(!isBookExistsInDatabase(book, em))
            throw new ValidationException("Knjiga ne postoji u sistemu!");
        if(isBookCurrentlyRented(book, em))
            throw new ValidationException("Knjiga je trenutno iznajmljena!");
        if(getCurrentMemberRents(member, em)>1)
            throw new ValidationException("Član trenutno beleži maksimali broj knjiga za iznajmljivanje - dve.");
    }

    private int getCurrentMemberRents(LibraryMember member,  EntityManager em) {
        List<BookRent> dbRents= (List<BookRent>) rentRepository.getByUser(member.getId(), em);
        int i=0;
        for (BookRent br: dbRents){
        if(br.getReturnDate()==null){
            i++;
        }
        }
        return i;
    }

    private boolean isBookCurrentlyRented(Book book,  EntityManager em) {
       Book dbBook= bookRepository.findById(book.getId(), em);
       return dbBook.isCurrentlyRented();

    }

    private boolean isBookExistsInDatabase(Book book,  EntityManager em) {
        Book dbBook=bookRepository.findById(book.getId(), em);
        if(dbBook==null) return false;
        return true;
    }

    private boolean isMemberCardExpiry(LibraryMember member) {
        if(member.getMembershipCard().getExpiryDate().isBefore(LocalDate.now())) return true;
        return false;
    }

    private boolean isMemberExistsInDatabase(LibraryMember member, EntityManager em) {
        LibraryMember dbMember=memberRepository.getById(member.getId(), em);
        if(dbMember==null) return false;
        return true;
    }
}
