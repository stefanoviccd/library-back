package rs.ac.bg.fon.libraryback.repository.impl;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public Librarian login(String username, String password) throws UserNotFoundException {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();

        List<Librarian> dbResult= em.createQuery("select m from Librarian m where m.username LIKE :valueUser and m.password LIKE :valuePass").setParameter("valueUser", username).setParameter("valuePass", password)
                .getResultList();
        if(dbResult.isEmpty())
            throw  new UserNotFoundException("Neispravno korisničko ime ili lozinka.");
        return dbResult.get(0);
    }
    public Librarian findByUsername(String username) throws UserNotFoundException {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();

        List<Librarian> dbResult= em.createQuery("select m from Librarian m where m.username LIKE :valueUser").setParameter("valueUser", username)
                .getResultList();
        if(dbResult.isEmpty())
            throw  new UserNotFoundException("Neispravno korisničko ime.");
        return dbResult.get(0);
    }
}
