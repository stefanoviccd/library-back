package rs.ac.bg.fon.libraryback.validation.impl;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.validation.BookValidator;

import javax.persistence.EntityManager;
import java.util.List;

public class DeleteBookValidator implements BookValidator {
    @Override
    public void validate(Object o) throws ValidationException {
        Long id=(Long) o;
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
       /* List<BookRent> rentedBooks=em.createQuery("select m from BookRent m where m.book.id= :id ")
                .setParameter("id", id).getResultList();
        for(BookRent br: rentedBooks){
            if(br.getReturnDate()!=null)
                throw  new ValidationException("Book is currently rented!");
        }*/
        Book dbBook=em.find(Book.class, id);
        if(dbBook.isCurrentlyRented())
            throw  new ValidationException("Book is currently rented!");




    }
}
