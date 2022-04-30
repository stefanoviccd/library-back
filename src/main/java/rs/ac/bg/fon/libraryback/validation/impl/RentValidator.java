package rs.ac.bg.fon.libraryback.validation.impl;

import org.apache.tomcat.jni.Library;
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

import java.lang.reflect.Member;
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
    public void validate(LibraryMember member, Book book) throws ValidationException {
        if(!isMemberExistsInDatabase(member))
            throw new ValidationException("Library member does not exists in database!");
        if(isMemberCardExpiry(member))
            throw new ValidationException("Membership card is expiry!");
        if(!isBookExistsInDatabase(book))
            throw new ValidationException("Book does not exists in database!");
        if(isBookCurrentlyRented(book))
            throw new ValidationException("Book is currently rented!");
        if(getCurrentMemberRents(member)>1)
            throw new ValidationException("Member currently has maximum allowed number of rented books - two.");
    }

    private int getCurrentMemberRents(LibraryMember member) {
        List<BookRent> dbRents= (List<BookRent>) rentRepository.getByUser(member.getId()).stream().filter(bookRent -> bookRent.getReturnDate()==null);
       /* for (BookRent br: dbRents){
        if(br.getReturnDate()==null){
            i++;
        }
        }*/
        return dbRents.size();
    }

    private boolean isBookCurrentlyRented(Book book) {
       Book dbBook= bookRepository.findById(book.getId());
       return dbBook.isCurrentlyRented();

    }

    private boolean isBookExistsInDatabase(Book book) {
        Book dbBook=bookRepository.findById(book.getId());
        if(dbBook==null) return false;
        return true;
    }

    private boolean isMemberCardExpiry(LibraryMember member) {
        if(member.getMembershipCard().getExpiryDate().isBefore(LocalDate.now())) return true;
        return false;
    }

    private boolean isMemberExistsInDatabase(LibraryMember member) {
        LibraryMember dbMember=memberRepository.getById(member.getId());
        if(dbMember==null) return false;
        return true;
    }
}
