package rs.ac.bg.fon.libraryback.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.dto.RentedBookInfo;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;

import java.util.List;

@Service
public interface IRentService {
    List<RentedBookInfo> getUserRents(Long userId, int page, int offset, String sortAttribute, String sortOrder) throws ValidationException;
    RentedBookInfo restoreBook(BookRentDTO rentDTO)throws ValidationException, UserAlreadyExistsException;
    RentedBookInfo rentBook(BookRentDTO rentDTO)throws ValidationException, UserAlreadyExistsException;
}
