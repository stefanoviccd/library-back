package rs.ac.bg.fon.libraryback.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.service.BookRentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/rents")
public class RentsController {
    @Autowired
    private BookRentService rentService;
    @Autowired
    private ModelMapper modelMapper;

    public RentsController() {
    }

    @CrossOrigin
    @PostMapping("/rent")
    public ResponseEntity<Response> rentBook(@RequestBody BookRentDTO rentDTO) {
        Response response = new Response();
        try {
            BookRent rent = modelMapper.map(rentDTO, BookRent.class);
            Book book = rent.getBook();
            LibraryMember member = rent.getByMember();
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
    public ResponseEntity<Response> restoreBook(@RequestBody BookRentDTO rentDTO) {
        Response response = new Response();
        try {
            BookRent rent = modelMapper.map(rentDTO, BookRent.class);
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
    public ResponseEntity<Response> getUserRents(@PathVariable(name = "id") Long userId) {
        Response response = new Response();
        try {
            List<BookRentDTO> userRents = rentService.getUserRents(userId).stream().map(ur -> modelMapper.map(ur, BookRentDTO.class)).collect(Collectors.toList());
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
