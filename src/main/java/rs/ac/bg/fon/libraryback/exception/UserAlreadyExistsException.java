package rs.ac.bg.fon.libraryback.exception;

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String message){
        super(message);
    }
}
