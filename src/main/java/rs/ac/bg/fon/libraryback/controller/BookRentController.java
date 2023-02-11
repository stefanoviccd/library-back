package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.dto.RentedBookInfo;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.service.impl.RentService;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("api/v1/rent")
public class BookRentController {
    @Autowired
    private RentService rentService;


    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> rentBook(@RequestBody BookRentDTO rentDTO) {
        Response response = new Response();
        try {
            RentedBookInfo rentedBookInfo=rentService.rentBook(rentDTO);
            response.setResponseData(rentedBookInfo);
            response.setResponseException(null);
            return ResponseEntity.created(URI.create("http://localhost:8089/api/v1/rent")).body(response);


        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }

    }

    @CrossOrigin
    @PatchMapping()
    public ResponseEntity<Response> restoreBook(@RequestBody BookRentDTO rentDTO) {
        Response response = new Response();
        try {
            RentedBookInfo rentedBookInfo=rentService.restoreBook(rentDTO);
            response.setResponseData(rentedBookInfo);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUserRents(@PathVariable(name = "id", required = true) Long userId,
                                                 @RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "10") int offset,
                                                 @RequestParam(required = false, defaultValue = "id") String sortAttribute,
                                                 @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        Response response = new Response();
        try {
            List<RentedBookInfo> userRents=rentService.getUserRents(userId, page, offset, sortAttribute, sortOrder);
            response.setResponseData(userRents);
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
}
