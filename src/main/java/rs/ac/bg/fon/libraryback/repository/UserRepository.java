package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;

public interface UserRepository {
    Librarian login(String username, String password) throws UserNotFoundException;

}
