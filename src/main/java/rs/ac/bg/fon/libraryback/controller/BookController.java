package rs.ac.bg.fon.libraryback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.service.BookService;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/books")
public class BookController {
    private BookService bookService;

    public BookController() {
        bookService = new BookService();
    }
    @CrossOrigin(origins = "*")
    @GetMapping
    public ResponseEntity<Response> getAllBooks() {
        Response response = new Response();
        try {
            List<Book> books = bookService.getAllBooks();
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

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
            System.out.println("POSLATI ZANROVI - velicine " +genres.length);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/{value}")
    @CrossOrigin
    public ResponseEntity<Response> getBookByValue(@PathVariable(name = "value") String value) {
        Response response = new Response();
        try {
            List<Book> books = bookService.getByValue(value);
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/id/{id}")
    @CrossOrigin
    public ResponseEntity<Response> getBookById(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            Book book= bookService.getById(id);
            response.setResponseData(book);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

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

        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            ex.printStackTrace();
            return ResponseEntity.ok().body(response);
        }

    }
    @PutMapping()
    public ResponseEntity<Response> updateBook(@RequestBody Book book) {

        Response response = new Response();
        try {
            Book savedBook = bookService.update(book);
            response.setResponseData(savedBook);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> saveBook(@RequestBody Book book) {
        Response response = new Response();
        try {
            System.out.println(book);
            Book save = bookService.save(book);
            response.setResponseData(save);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.ok().body(response);
        }
    }

}
