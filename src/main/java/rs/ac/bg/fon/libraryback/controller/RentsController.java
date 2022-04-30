package rs.ac.bg.fon.libraryback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.service.BookRentService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rents")
public class RentsController {
    private BookRentService rentService;

    public RentsController() {
        rentService = new BookRentService();
    }
    @CrossOrigin
    @PostMapping("/rent")
    public ResponseEntity<Response> rentBook(@RequestParam(name = "member") LibraryMember member,
                                             @RequestParam(name = "book") Book book) {
        Response response = new Response();
        try {
            rentService.rentBook(member, book);
            response.setResponseData(null);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }

    }
    @CrossOrigin
    @PostMapping("/restore")
    public ResponseEntity<Response> restoreBook(@RequestParam(name = "rent") BookRent rent
                                         ) {
        Response response = new Response();
        try {
            rentService.restoreBook(rent);
            response.setResponseData(null);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }

    }
}
