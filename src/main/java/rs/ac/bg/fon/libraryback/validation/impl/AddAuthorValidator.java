package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.repository.AuthorRepository;
import rs.ac.bg.fon.libraryback.repository.impl.AuthorRepositoryImpl;
import rs.ac.bg.fon.libraryback.validation.AuthorValidator;

import java.util.List;
public class AddAuthorValidator implements AuthorValidator {
    private AuthorRepository authorRepository;
    public AddAuthorValidator(){
        authorRepository=new AuthorRepositoryImpl();
    }
    @Override
    public void validate(Object o) throws ValidationException {
        Author author=(Author) o;
        List<Author>  authors=authorRepository.getByFullName(author.getName(), author.getLastName());
        if(!authors.isEmpty())
            throw new ValidationException("Autor" +author.getName()+" "+author.getLastName() +" postoji u sistemu!");

    }
}
