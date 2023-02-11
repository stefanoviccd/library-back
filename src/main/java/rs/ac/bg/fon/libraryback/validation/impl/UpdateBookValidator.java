package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.repository.IBookGenreRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.util.NoSuchElementException;
@Component
public class UpdateBookValidator implements IValidator {
    @Autowired
    public IBookRepository bookRepository;
    @Autowired
    public IBookGenreRepository bookGenreRepository;
    @Override
    public void validate(Object o) throws ValidationException {
        BookDTO bookDTO= (BookDTO) o;
        if(bookDTO==null) throw new ValidationException("No data is passed!");
        if(bookDTO.getISBN()==null || bookDTO.getISBN().isEmpty() ||
                bookDTO.getTitle()==null || bookDTO.getTitle().isEmpty() ||
                bookDTO.getIssueYear()==0 ||
                bookDTO.getGenre()==null || bookDTO.getGenre().isEmpty() ||
                bookDTO.getAuthorsName()==null || bookDTO.getAuthorsName().isEmpty() ||
                bookDTO.getAuthorsLastName()==null || bookDTO.getAuthorsLastName().isEmpty() )
            throw new ValidationException("All required fields must be passed!");
        Book dbBookById=bookRepository.findById(bookDTO.getId()).get();
        Book dbBook= bookRepository.findByISBN(bookDTO.getISBN());
        if(dbBook!=null && dbBook.getId()!=bookDTO.getId()) throw  new ValidationException("Book with isbn "+bookDTO.getISBN()+" already exists!");
        if(dbBook!=null && dbBook.isCurrentlyRented()) throw  new ValidationException("Cannot update book baceuse it is currently rented!");

        BookGenre genre=bookGenreRepository.findByName(bookDTO.getGenre());
        if(genre==null) throw new NoSuchElementException("Genre is invalid!");
    }
}
