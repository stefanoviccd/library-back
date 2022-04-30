package rs.ac.bg.fon.libraryback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.service.UserService;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;
    public UserController(){
        userService=new UserService();
    }
    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<Response> login(@RequestBody Librarian user) {

        Librarian dbLibrarian = null;
        Response response=new Response();
        try {
            dbLibrarian = userService.login(user.getUsername(), user.getPassword());
            response.setResponseData(dbLibrarian);
            response.setResponseException(null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setResponseData(null);
            response.setResponseException(e);
            e.printStackTrace();
            return ResponseEntity.ok(response);
        }

    }

}
