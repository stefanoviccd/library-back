package rs.ac.bg.fon.libraryback.repository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.repository.AuthorRepository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {
    @Override
    public void delete(Author dbAuthor, EntityManager em) throws ValidationException {
        Author author = em.find(Author.class, dbAuthor.getId());
        em.remove(author);

    }

    @Override
    public List<Author> getByFullName(String name, String lastName, EntityManager em) {
        List<Author> authors = em.createQuery("select m from Author m where m.name LIKE :name and  m.lastName LIKE :lastName").setParameter("name", name)
                .setParameter("lastName", lastName)
                .getResultList();
        return authors;
    }

    @Override
    public void save(Author author, EntityManager em) throws ValidationException {
        em.persist(author);
    }
}
