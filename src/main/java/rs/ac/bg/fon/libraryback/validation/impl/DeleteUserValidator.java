package rs.ac.bg.fon.libraryback.validation.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.Role;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IBookRentRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.util.List;

@Component
@Slf4j
public class DeleteUserValidator implements IValidator {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IBookRentRepository bookRentRepository;
    @Override
    public void validate(Object o) throws ValidationException, UserAlreadyExistsException {
        Long id= (Long) o;
        if(id==null){
            log.error("Delete user: No data passed!");
            throw new ValidationException("No data passed!");
        }
        User dbUser=userRepository.findById(id).get();
        if(dbUser==null){
            log.error("User with id \"+id+\" does not exist!");
            throw new ValidationException("User with id "+id+" does not exist!");
        }
        if(isAdmin(dbUser)){
            log.error("Cannot delete admin user!");
            throw new ValidationException("Cannot delete admin user!");
        }
        if(hasRentedBooks(id)){
            throw new ValidationException("Cannot delete user who currently rent books!");
        }
    }
    private boolean isAdmin(User dbUser) {
        for (Role r: dbUser.getRoles()
        ) {
            if(r.getName().equals("ROLE_ADMIN")){
                return true;
            }

        }
        return false;
    }
    private boolean hasRentedBooks(Long id){
        List<BookRent> rentedBooks=bookRentRepository.findByUser(id);
        for(BookRent br: rentedBooks){
            if(br.getReturnDate()==null)
                 return true;  }
        return false;
    }
}
