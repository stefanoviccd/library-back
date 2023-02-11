package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IBookRentRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.util.NoSuchElementException;
@Component
public class RestoreBookValidator implements IValidator {
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
            try{
                User dbUser=userRepository.findById(rent.getUserId()).get();
            }
            catch(NoSuchElementException e){
                throw new ValidationException("User with id "+rent.getBookId()+" does not exist in database!");
            }

        }
        catch(NoSuchElementException e){
            throw new ValidationException("Book with id "+rent.getBookId()+" does not exist in database!");
        }
    }
}
