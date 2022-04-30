package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.repository.BookRentRepository;
import rs.ac.bg.fon.libraryback.repository.impl.BookRentRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.LibraryMemberValidator;

import java.util.List;

public class DeleteMemberValidator implements LibraryMemberValidator {
    private BookRentRepository bookRentRepository;
    public DeleteMemberValidator(){
        bookRentRepository=new BookRentRepositoryImpl();
    }
    @Override
    public void validate(Object o) throws ValidationException {
        LibraryMember member=(LibraryMember) o;
          List<BookRent> rentedBooks=bookRentRepository.getByUser(member.getId());
        for(BookRent br: rentedBooks){
            if(br.getReturnDate()==null)
                throw  new ValidationException("Library member cannot be deleted because there are books currently rented!");
        }

    }
}
