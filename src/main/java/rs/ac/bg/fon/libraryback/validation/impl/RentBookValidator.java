package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.*;
import rs.ac.bg.fon.libraryback.repository.IBookRentRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class RentBookValidator  implements IValidator {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IBookRentRepository rentRepository;
    @Override
    public void validate(Object o) throws ValidationException, UserAlreadyExistsException {
        BookRentDTO rent= (BookRentDTO) o;
        if(rent==null || rent.getBookId()==null || rent.getUserId()==null){
            throw new ValidationException("No required data presented!");
        }
        try{
            Book dbBook=bookRepository.findById(rent.getBookId()).get();
            if(dbBook.isCurrentlyRented())
                throw new ValidationException("Book is currently rented!");
            try{
                User dbUser=userRepository.findById(rent.getUserId()).get();
                if(isAdmin(dbUser))
                    throw new ValidationException("You cannot rent book as admin of the system!");
                if(dbUser.getMembershipCard().getExpiryDate().isBefore(LocalDate.now()))
                    throw new ValidationException("User card is expired!");
                if(maxNumberOfRentedBooksExceeded(dbUser.getId()))
                    throw new ValidationException("Max number of rented books is exceeded!");



            }
            catch(NoSuchElementException e){
                throw new ValidationException("User with id "+rent.getUserId()+" does not exist in database!");
            }

        }
        catch(NoSuchElementException e){
            throw new ValidationException("Book with id "+rent.getBookId()+" does not exist in database!");
        }

    }

    private boolean isAdmin(User dbUser) {
        for(Role r: dbUser.getRoles()){
            if(r.getName().equalsIgnoreCase("ROLE_ADMIN")) return true;
        }
        return false;
    }

    private boolean maxNumberOfRentedBooksExceeded(Long id) {
        List<BookRent> dbRents= (List<BookRent>) rentRepository.getActiveByUser(id);
        return dbRents!=null && dbRents.size() >=2;

    }
}
