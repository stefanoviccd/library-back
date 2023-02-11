package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.util.NoSuchElementException;

@Component
public class DeleteBookValidator implements IValidator {
    @Autowired
    private IBookRepository bookRepository;
    @Override
    public void validate(Object o) throws ValidationException {
        Long id= (Long) o;
        try{
            Book dbBook=bookRepository.findById(id).get();
            if(dbBook.isCurrentlyRented()){
                throw new ValidationException("Cannot delete book because it is currently rented!");
            }
        }
        catch(NoSuchElementException e){
            throw new ValidationException("Invalid id passed!");
        }



    }
}
