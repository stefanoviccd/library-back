package rs.ac.bg.fon.libraryback.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.service.BookRentService;
import rs.ac.bg.fon.libraryback.service.LibraryMemberService;

import java.nio.file.Path;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/librarianMember")
public class LibraryMemberController {

    private LibraryMemberService memberService;
    private BookRentService rentService;
    public LibraryMemberController() {
        memberService = new LibraryMemberService();
        rentService=new BookRentService();
    }

    @GetMapping
    public ResponseEntity<Response> getAllMembers() {
        Response response = new Response();
        try {
            List<LibraryMember> members = memberService.getAllMembers();
            response.setResponseData(members);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }
    @GetMapping("{id}/rents")
    public ResponseEntity<Response> getUserRents(@PathVariable(name = "id") Long id ) {
        Response response = new Response();
        try {
            List<BookRent> rents = rentService.getUserRents(id);
            response.setResponseData(rents);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/{value}")
    @CrossOrigin
    public ResponseEntity<Response> getMembersByValue(@PathVariable(name = "value") String value) {
        Response response = new Response();
        try {
            List<LibraryMember> books = memberService.getByValue(value);
            response.setResponseData(books);
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
    public ResponseEntity<Response> deleteMember(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            memberService.deleteMember(id);
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
    public ResponseEntity<Response> updateMember(@RequestBody LibraryMember member) {

        Response response = new Response();
        try {
            LibraryMember dbMember = memberService.update(member);
            response.setResponseData(dbMember);
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
    public ResponseEntity<Response> saveMember(@RequestBody LibraryMember member) {
        Response response = new Response();
        try {
            LibraryMember save = memberService.save(member);
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
