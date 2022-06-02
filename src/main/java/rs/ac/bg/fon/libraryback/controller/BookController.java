package rs.ac.bg.fon.libraryback.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.service.BookService;
import rs.ac.bg.fon.libraryback.service.impl.BookServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/books")
public class BookController {
    @Autowired
    private BookService bookService;
    public BookController() {
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<Response> getAllBooks() {
        Response response = new Response();
        try {
            List<BookDTO> books = bookService.getAllBooks();
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }
    @GetMapping("/genres")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Response> getBookGenres() {
        Response response = new Response();
        try {
            BookGenre[] genres = BookGenre.values();
            response.setResponseData(genres);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }

    @GetMapping("/{value}")
    @CrossOrigin
    public ResponseEntity<Response> getBookByValue(@PathVariable(name = "value") String value) {
        Response response = new Response();
        try {
            List<BookDTO> books = bookService.getByValue(value);
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }

    @GetMapping("/id/{id}")
    @CrossOrigin
    public ResponseEntity<Response> getBookById(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            BookDTO book= bookService.getById(id);
            response.setResponseData(book);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (ValidationException ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteBook(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            bookService.deleteBook(id);
            response.setResponseData(null);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);

        } catch (ValidationException ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }

    }
    @PutMapping()
    public ResponseEntity<Response> updateBook(@RequestBody BookDTO bookDTO) {

        Response response = new Response();
        try {
            BookDTO savedBook = bookService.update(bookDTO);
            response.setResponseData(savedBook);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (ValidationException ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> saveBook(@RequestBody BookDTO bookDTO) {
        Response response = new Response();
        try {
            BookDTO save = bookService.save(bookDTO);
            response.setResponseData(save);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);


        } catch (ValidationException ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }

}
