package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dbConnection.EntityManagerProvider;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.dto.LibraryMemberDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public interface BookRentService {
    List<BookRentDTO> getUserRents(Long id) throws ValidationException;
    void rentBook(LibraryMember member, Book book) throws ValidationException;

    void restoreBook(BookRentDTO rent) throws ValidationException;
}
