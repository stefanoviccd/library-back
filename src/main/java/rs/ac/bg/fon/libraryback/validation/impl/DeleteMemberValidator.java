package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import javax.persistence.EntityManager;
import java.util.List;
public class DeleteMemberValidator implements LibraryMemberValidator {
    private BookRentRepository bookRentRepository;
    public DeleteMemberValidator(){
        bookRentRepository=new BookRentRepositoryImpl();
    }
    @Override
    public void validate(Object o, EntityManager em) throws ValidationException {
        Long memberId=(Long) o;
          List<BookRent> rentedBooks=bookRentRepository.getByUser(memberId, em);
        for(BookRent br: rentedBooks){
            if(br.getReturnDate()==null)
                throw  new ValidationException("Ne možete izbrisati člana jer trenutno postoje knjige koje iznajmljuje!");
        }

    }
}
