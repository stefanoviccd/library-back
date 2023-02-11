package rs.ac.bg.fon.libraryback.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.BookSummaryDTO;
import rs.ac.bg.fon.libraryback.dto.mapper.EntityToDtoMapper;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.repository.IAuthorRepository;
import rs.ac.bg.fon.libraryback.repository.IBookGenreRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRentRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.service.IBookService;
import rs.ac.bg.fon.libraryback.validation.impl.AddBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateBookValidator;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService implements IBookService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IBookGenreRepository bookGenreRepository;
    @Autowired
    private IBookRentRepository bookRentRepository;
    @Autowired
    private IAuthorRepository authorRepository;
    @Autowired
    private AddBookValidator addBookValidator;
    @Autowired
    private UpdateBookValidator updateBookValidator;

    private EntityToDtoMapper mapper = new EntityToDtoMapper();

    @Override
    public List<BookDTO> getAll(int page, int offset, String sortAttribute, String sortOrder) {
        try {
            Sort sort;
            if(sortOrder.equalsIgnoreCase("desc"))
                sort=Sort.by(Sort.Direction.DESC, sortAttribute);
            else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
            Pageable p = PageRequest.of(page, offset, sort);
            List<Book> dbBooks = bookRepository.findAll(p).getContent();
            List<BookDTO> dtos = new ArrayList<>();
            for (Book b : dbBooks) {
                dtos.add(mapper.toDto(b));
            }
            return dtos;
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public BookDTO getById(Long id) throws Exception {
        try {
            if (id == null) {
                throw new ValidationException("No id is passed!");
            }
            if(id<0){
                throw new ValidationException("Invalid value for book id! Please provide correct value.");
            }
            Book dbBook = bookRepository.findById(id).get();
            if (dbBook == null) {
                throw new ValidationException("Book with id "+id+" does not exist in database!");
            } else {
                return mapper.toDto(dbBook);
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("Book with id " + id + " does not exist in database!");
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            if (id == null) {
                throw new ValidationException("No id is passed!");
            }
            Book dbBook = bookRepository.findById(id).get();
            bookRepository.delete(dbBook);

        } catch (ValidationException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw new Exception("Book with id " + id + " does not exist in database!");
        }
    }

    @Override
    public BookDTO save(BookDTO bookDTO) throws Exception {
        addBookValidator.validate(bookDTO);
        Author author = authorRepository.findByNameAndLastName(bookDTO.getAuthorsName(), bookDTO.getAuthorsLastName());
        if (author == null) {
            author = new Author();
            author.setName(bookDTO.getAuthorsName());
            author.setLastName(bookDTO.getAuthorsLastName());
            authorRepository.save(author);
        }
        BookGenre genre = bookGenreRepository.findByName(bookDTO.getGenre());
        Book book = new Book();
        book.setAuthor(author);
        book.setGenre(genre);
        book.setISBN(bookDTO.getISBN());
        book.setTitle(bookDTO.getTitle());
        book.setIssueYear(bookDTO.getIssueYear());
        book.setCurrentlyRented(false);

        bookRepository.save(book);
        bookDTO.setId(book.getId());
        return bookDTO;


    }

    @Override
    public List<BookGenre> getGenres(int page, int offset, String sortAttribute, String sortOrder) {
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
        List<BookGenre> genres = bookGenreRepository.findAll(p).getContent();
        return genres;

    }

    @Override
    public BookDTO update(BookDTO bookDTO) throws ValidationException {
        updateBookValidator.validate(bookDTO);
        Author author = authorRepository.findByNameAndLastName(bookDTO.getAuthorsName(), bookDTO.getAuthorsLastName());
        if (author == null) {
            author = new Author();
            author.setName(bookDTO.getAuthorsName());
            author.setLastName(bookDTO.getAuthorsLastName());
            authorRepository.save(author);
        }
        BookGenre genre = bookGenreRepository.findByName(bookDTO.getGenre());
        Book book = bookRepository.findById(bookDTO.getId()).get();
        book.setAuthor(author);
        book.setGenre(genre);
        book.setISBN(bookDTO.getISBN());
        book.setTitle(bookDTO.getTitle());
        book.setIssueYear(bookDTO.getIssueYear());
        bookRepository.save(book);
        bookDTO.setId(book.getId());
        return bookDTO;

    }

    @Override
    public List<BookSummaryDTO> getSummary(int page, int offset, String sortAttribute, String sortOrder) {
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
        List<?> res = bookRepository.getSummary(p);
        List<BookSummaryDTO> summary = new ArrayList<>();
        if (res != null && res.size() > 0) {
            for (Object row : res
            ) {
                Object asArray[] = (Object[]) row;
                BookSummaryDTO bookSymmary = new BookSummaryDTO();
                bookSymmary.setTotalCount((BigInteger) asArray[0]);
                bookSymmary.setTitle((String) asArray[1]);
                BigInteger genreId = (BigInteger) asArray[3];
                BigInteger authorId = (BigInteger) asArray[2];
                BookGenre genre = bookGenreRepository.findById(genreId.longValue()).get();
                bookSymmary.setGenre(genre.getName());
                Author author = authorRepository.findById(authorId.longValue()).get();
                bookSymmary.setAuthor(author.getName() + " " + author.getLastName());
                List<Book> rented = bookRepository.findCurrentlyRentedCount(bookSymmary.getTitle(), author.getId());

                if (rented != null) {
                    bookSymmary.setCurrentlyRentedCount(rented.size());
                }

                summary.add(bookSymmary);


            }
        }
        return summary;
    }

    @Override
    public List<BookDTO> getByValue(String value, int page, int offset, String sortAttribute, String sortOrder) throws ValidationException {
        if(page<0)
            throw new ValidationException("Invalid value for page number!");
        if(offset<0)
            throw new ValidationException("Invalid value for limit!");
        String searchValue="%"+value+"%";
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
            List<Book> dbBooks = bookRepository.findByValue(searchValue,p);
            if (dbBooks == null) {
                return new ArrayList<>();
            }
            List<BookDTO> dtos = new ArrayList<>();
            for (Book b : dbBooks
            ) {
                dtos.add(mapper.toDto(b));
            }
            return dtos;

    }
}
