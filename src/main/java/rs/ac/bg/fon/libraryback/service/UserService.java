package rs.ac.bg.fon.libraryback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.refactor.UserRepository;

import javax.persistence.EntityManager;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public UserService() {
    }

    public Librarian login(String username, String password) throws UserNotFoundException, ValidationException {
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        try {
            if (username == null && username.isEmpty()) {
                throw new ValidationException("Username ne sme null!");
            }
            if (password == null && password.isEmpty()) {
                throw new ValidationException("Password ne sme null!");
            }
            Librarian dbLibrarian = userRepository.login(username, password);
            em.getTransaction().commit();
            if(dbLibrarian==null) throw new UserNotFoundException("Neispravno korisničko ime ili lozinka.");
            return dbLibrarian;


        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();
        }


    }

}
