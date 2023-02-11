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
public class AddBookValidator implements IValidator {
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
            throw new ValidationException("All required fields must be passed!\nEnsure you are passing value for:\n" +
                    "Title\nIssueYear\nGenre\nAuthorsName\nAuthorsLastName\n");
        Book dbBook= bookRepository.findByISBN(bookDTO.getISBN());
        if(dbBook!=null) throw  new ValidationException("Book with isbn "+bookDTO.getISBN()+" already exists!");
        BookGenre genre=bookGenreRepository.findByName(bookDTO.getGenre());
        if(genre==null) throw new NoSuchElementException("Genre is invalid!");
    }
}
