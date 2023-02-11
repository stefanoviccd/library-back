package rs.ac.bg.fon.libraryback.validation.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IMembershipCardRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.validation.IValidator;

import java.time.LocalDate;

@Component
public class UpdateUserValidator implements IValidator {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IMembershipCardRepository cardRepository;
    @Override
    public void validate(Object o) throws ValidationException, UserAlreadyExistsException {
        User user= (User) o;
        if(user==null){
            throw new ValidationException("No data is passed!");
        }
        if(user.getId()==null){
            throw new ValidationException("User id must be provided for updating!");
        }
        if(user.getFirstName()!=null && user.getFirstName().isEmpty()){
            throw new ValidationException("First name cannot be empty!");
        }
        if(user.getLastName()!=null && user.getLastName().isEmpty()){
            throw new ValidationException("Last name cannot be empty!");
        }
        if(user.getContact()!=null && user.getContact().isEmpty()){
            throw new ValidationException("Contact cannot be empty!");
        }
        if(user.getUsername()!=null && user.getUsername().isEmpty()){
            throw new ValidationException("Username cannot be empty!");
        }
        if(user.getPassword()!=null && user.getPassword().isEmpty()){
            throw new ValidationException("Password cannot be empty!");
        }
        if(user.getPassword()!=null && isUsernameTaken(user.getUsername(), user.getId())){
            throw new ValidationException("Username "+user.getUsername()+" is already taken!");
        }
        if(user.getMembershipCard()!=null && !isOfRequiredFormat(user.getMembershipCard().getCardNumber())){
            throw new ValidationException("Membership card is of invalid format!\nReguired format: all digits.\nRequired length: 15.");
        }
        if(user.getMembershipCard()!=null && isInUse(user.getMembershipCard().getCardNumber(), userRepository.findById(user.getId()).get().getMembershipCard()==null ? null : userRepository.findById(user.getId()).get().getMembershipCard().getId())){
            throw new ValidationException("Membership card with provided number is taken!");
        }
    }

    private boolean isInUse(String cardNumber, Long id) {
        MembershipCard dbCard= cardRepository.findByCardNumber(cardNumber).isPresent() ? cardRepository.findByCardNumber(cardNumber).get() : null;
        if(dbCard==null) return false;
        if(dbCard.getExpiryDate().isBefore(LocalDate.now())) return false;
        if(id!=null && dbCard.getId().equals(id)) return false;
        return true;
    }

    private boolean isOfRequiredFormat(String cardNumber) {
        return (cardNumber.length()==15 && isOfDigits(cardNumber));
    }

    private boolean isOfDigits(String cardNumber) {
       for(int i=0;i<cardNumber.length();i++){
           if(!Character.isDigit(cardNumber.charAt(i)))
               return false;
       }
       return true;
    }

    private boolean isUsernameTaken(String username, Long id) {
        User user = userRepository.findByUsername(username);
        return user!=null && !user.getId().equals(id);
    }
}
