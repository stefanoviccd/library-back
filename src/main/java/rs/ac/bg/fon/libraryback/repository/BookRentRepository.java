package rs.ac.bg.fon.libraryback.repository;

import rs.ac.bg.fon.libraryback.model.BookRent;

import java.util.List;

public interface BookRentRepository {
    List<BookRent> getByUser(Long userId);

    BookRent save(BookRent bookRent);

    void update(BookRent bookRent);
}
