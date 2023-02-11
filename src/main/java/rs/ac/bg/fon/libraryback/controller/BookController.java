package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.constants.Constants;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.BookSummaryDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.BookGenre;
import rs.ac.bg.fon.libraryback.service.IBookService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping()
    public ResponseEntity<Response> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int offset,
                                           @RequestParam(required = false, defaultValue = "title") String sortAttribute,
                                           @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        Response response = new Response();
        try {
            List<BookDTO> books = bookService.getAll(page, offset, sortAttribute,sortOrder);
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getSummary(@RequestParam(required = false, defaultValue = "0") int page,
                                               @RequestParam(required = false, defaultValue = "10") int offset,
                                               @RequestParam(required = false, defaultValue = "title") String sortAttribute,
                                               @RequestParam(required = false, defaultValue = "asc") String sortOrder
                                               ) {
        Response response = new Response();
        try {
            List<BookSummaryDTO> books = bookService.getSummary(page, offset, sortAttribute, sortOrder);
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }


    @GetMapping("/genres")
    public ResponseEntity<Response> getBookGenres(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int offset,
            @RequestParam(required = false, defaultValue = "name") String sortAttribute,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        Response response = new Response();
        try {
            List<BookGenre> books = bookService.getGenres(page, offset, sortAttribute,sortOrder);
            response.setResponseData(books);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @GetMapping("/search")
    public ResponseEntity<Response> getBookByValue(@RequestParam(required = false, defaultValue = "") String value,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int offset,
                                                   @RequestParam(required = false, defaultValue = "title") String sortAttribute,
                                                   @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        Response response = new Response();
        try {
            List<BookDTO> book = bookService.getByValue(value, page, offset, sortAttribute,sortOrder);
            response.setResponseData(book);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ValidationException e){
            response.setResponseData(null);
            response.setResponseException(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getById(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            BookDTO book = bookService.getById(id);
            response.setResponseData(book);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ValidationException e){
            response.setResponseData(null);
            response.setResponseException(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteBook(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            bookService.delete(id);
            return ResponseEntity.noContent().build();

        }  catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PutMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> updateBook(@RequestBody BookDTO bookDTO) {
        Response response = new Response();
        try {
            BookDTO updated = bookService.update(bookDTO);
            response.setResponseData(updated);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> saveBook(@RequestBody BookDTO bookDTO) {
        Response response = new Response();
        try {
            BookDTO save = bookService.save(bookDTO);
            response.setResponseData(save);
            response.setResponseException(null);
            return ResponseEntity.created(URI.create(Constants.SAVE_BOOK_URI)).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }

}
