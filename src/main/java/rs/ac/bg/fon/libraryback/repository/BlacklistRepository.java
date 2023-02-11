package rs.ac.bg.fon.libraryback.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import rs.ac.bg.fon.libraryback.model.Blacklist;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BlacklistRepository {
    public Blacklist getByToken(String name){
        try{
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            EntityManager em= session.getEntityManagerFactory().createEntityManager();
            Blacklist token = (Blacklist) em.createQuery("select b from Blacklist b where b.token = :value").setParameter("value", name)
                    .getSingleResult();
            em.close();
            return token;
        }
        catch (NoResultException e){
            return null;
        }
        catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }


    }

    public Blacklist saveToBlacklist(String name){
        try{
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            EntityManager em= session.getEntityManagerFactory().createEntityManager();
            Blacklist token = new Blacklist();
            em.getTransaction().begin();
            token.setToken(name);
            token.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
            em.persist(token);
            em.getTransaction().commit();
            em.close();
            return token;
        }
        catch (NoResultException e){
            return null;
        }
        catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            return null;
        }


    }

}
