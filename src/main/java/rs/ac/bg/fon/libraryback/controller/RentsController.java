package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private BookRentService rentService;

    public RentsController() {
    }
    @CrossOrigin
    @PostMapping("/rent")
    public ResponseEntity<Response> rentBook(@RequestBody  BookRent rent
                                            ) {
        Response response = new Response();
        try {
            Book book=rent.getBook();
            LibraryMember member=rent.getByMember();
            rentService.rentBook(member, book);
            response.setResponseData(null);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            ex.printStackTrace();
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }

    }
    @CrossOrigin
    @PostMapping("/restore")
    public ResponseEntity<Response> restoreBook(@RequestBody BookRent rent
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

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserRents(@PathVariable(name = "id") Long userId
    ) {
        Response response = new Response();
        try {
            List<BookRent> userRents=rentService.getUserRents(userId);

            response.setResponseData(userRents);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }

    }




}
