package rs.ac.bg.fon.libraryback.service;


import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import javax.persistence.EntityManager;

public class UserService {
    private UserRepository userRepository;
    public UserService(){
        userRepository=new UserRepositoryImpl();
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

            Librarian dbLibrarian=userRepository.login(username, password);
            em.getTransaction().commit();
            return dbLibrarian;


        } catch (Exception e) {
            em.getTransaction().rollback();

            throw  e;
        }
        finally {
            em.close();
            EntityManagerProvider.getInstance().closeSession();


        }



    }

}
