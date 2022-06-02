package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Librarian login(String username, String password){
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();

        List<Librarian> dbResult= em.createQuery("select m from Librarian m where m.username LIKE :valueUser and m.password LIKE :valuePass").setParameter("valueUser", username).setParameter("valuePass", password)
                .getResultList();
        if(dbResult.isEmpty())
           return null;
        return dbResult.get(0);
    }
    public Librarian findByUsername(String username)  {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();

        List<Librarian> dbResult= em.createQuery("select m from Librarian m where m.username LIKE :valueUser").setParameter("valueUser", username)
                .getResultList();
        if(dbResult.isEmpty())
            return null;
        return dbResult.get(0);
    }
}
