package rs.ac.bg.fon.libraryback.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;

public class CustomAuthenticationManager implements AuthenticationManager {
    @Autowired
    private UserRepository repo;

    public CustomAuthenticationManager(){
        repo=new UserRepositoryImpl();

    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        try {
            em.getTransaction().begin();
            Librarian lib=repo.login(name, password);
            em.getTransaction().commit();
            if(lib==null){
                throw new UserNotFoundException("Neispravno korisničko ime ili lozinka.");
            }
            return new UsernamePasswordAuthenticationToken(
                    name, password, new ArrayList<>());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        finally{
            em.close();
        }
    }
}
