package rs.ac.bg.fon.libraryback.dbConnection;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;

public class EntityManagerProvider {
    private static EntityManagerProvider instance;
    private static EntityManager em;
    private static Session session;
    private SessionFactory sessionFactory;
    private EntityManagerProvider(){
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();


    }
    public static EntityManagerProvider getInstance(){
        if(instance==null) instance=new EntityManagerProvider();
        return instance;
    }
    public EntityManager getEntityManager(){
        em= session.getEntityManagerFactory().createEntityManager();

        return em;

    }



}
