package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.repository.AuthorRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    @Override
    public void delete(Author dbAuthor) throws ValidationException {
        if (dbAuthor == null) {
            throw new ValidationException("Autor pri brisanju ne sme biti null!");
        }
        if (dbAuthor.getId() == null) {
            throw new ValidationException("Autor za brisanje ne sme imati id null!");
        }
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Author author = em.find(Author.class, dbAuthor.getId());
        em.remove(author);

    }

    @Override
    public List<Author> getByFullName(String name, String lastName) {
        EntityManager em=EntityManagerProvider.getInstance().getEntityManager();
        List<Author> authors = em.createQuery("select m from Author m where m.name LIKE :name and  m.lastName LIKE :lastName").setParameter("name", name)
                .setParameter("lastName", lastName)
                .getResultList();
        return authors;
    }

    @Override
    public void save(Author author) throws ValidationException {
        if (author == null) {
            throw new ValidationException("Autor za čuvanje je null!");
        }
        if (author.getName() == null || author.getLastName() == null) {
            throw new ValidationException("Autor za čuvanje nema validno ime ili prezime!");
        }

        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.persist(author);
    }
}
