package rs.ac.bg.fon.libraryback.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;

public class CustomAuthenticationManager implements AuthenticationManager {
    private UserRepository repo;

    public CustomAuthenticationManager(){
        repo=new UserRepositoryImpl();

    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
            Librarian lib=repo.login(name, password);
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
