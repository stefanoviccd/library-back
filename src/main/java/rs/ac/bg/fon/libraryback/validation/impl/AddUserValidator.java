package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.dto.RegisterDTO;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

@Component
public class AddUserValidator implements IValidator {
    @Autowired
    private IUserRepository userRepository;
    @Override
    public void validate(Object o) throws ValidationException, UserAlreadyExistsException {
        RegisterDTO registerDTO= (RegisterDTO) o;
        if(registerDTO==null){
            throw new ValidationException("User registration: No data!");
        }
        if(registerDTO.getFirstName()==null || registerDTO.getFirstName().isEmpty() ||
                registerDTO.getLastName()==null || registerDTO.getLastName().isEmpty() ||
                registerDTO.getContact()==null || registerDTO.getContact().isEmpty() ||
                registerDTO.getUsername()==null || registerDTO.getUsername().isEmpty() ||
                registerDTO.getPassword()==null || registerDTO.getPassword().isEmpty()

        )
            throw new ValidationException("All required fields must be passed!\\nEnsure you are passing value for:\n" +
                    "FirstName\nLastName\nContact\nUsername\nPassword\n");
        if(registerDTO.getPassword().length()<8)
            throw new ValidationException("Password fields must be at least 8 characters length!");

        User dbUser=userRepository.findByUsername(registerDTO.getUsername());
        if(dbUser !=null)
            throw new UserAlreadyExistsException("Username is already taken!");
    }
}
