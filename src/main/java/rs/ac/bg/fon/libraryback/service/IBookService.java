package rs.ac.bg.fon.libraryback.service;

import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.BookSummaryDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookGenre;

import java.sql.SQLException;
import java.util.List;

@Service
public interface IBookService {
    List<BookDTO> getAll(int page, int offset,  String sortAttribude, String sortOrder);

    BookDTO getById(Long id) throws Exception;

    void delete(Long id) throws Exception;

    BookDTO save(BookDTO bookDTO) throws Exception;

    List<BookGenre> getGenres(int page, int offset, String sortAttribude, String sortOrder);

    BookDTO update(BookDTO bookDTO) throws ValidationException;

    List<BookSummaryDTO> getSummary(int page, int offset, String sortAttribute, String sortOrder) throws SQLException;

    List<BookDTO> getByValue(String value, int page, int offset, String sortAttribude, String sortOrder) throws ValidationException;
}
