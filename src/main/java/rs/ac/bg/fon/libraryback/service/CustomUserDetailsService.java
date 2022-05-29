package rs.ac.bg.fon.libraryback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import javax.persistence.EntityManager;
import java.util.ArrayList;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    CustomUserDetailsService(){
      }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityManager em= EntityManagerProvider.getInstance().getEntityManager();
        if(!em.getTransaction().isActive())
            em.getTransaction().begin();
        try {
            Librarian u=userRepository.findByUsername(username);
            em.getTransaction().commit();
            return new User(u.getUsername(), u.getPassword(), new ArrayList<>());
        } catch (UserNotFoundException e) {
            em.getTransaction().rollback();
            throw new RuntimeException(e);

        }
        finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }
}
