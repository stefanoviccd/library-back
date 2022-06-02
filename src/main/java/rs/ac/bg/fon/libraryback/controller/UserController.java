package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.auth.CustomAuthenticationManager;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.service.impl.UserDetailsServiceImpl;
import rs.ac.bg.fon.libraryback.utility.JWTUtility;

import java.util.ArrayList;
import java.util.Date;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private JWTUtility jwtUtility;
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    public UserController() {
        authenticationManager = new CustomAuthenticationManager();
    }


    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<Response> login(@RequestBody Librarian user) {

        Librarian dbLibrarian = null;
        Response response = new Response();
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
         /*  if(auth==null){
               response.setResponseData(null);
               Exception ex = new Exception("Neispravno korisničko ime ili lozinka.");
               response.setResponseException(ex);
               return ResponseEntity.ok(response);
           }*/
            String token = jwtUtility.generateToken(user);
            Date expirationDate=jwtUtility.getExpirationDateFromToken(token);
            ArrayList<Object> authData=new ArrayList<>();
            authData.add(token);
            authData.add(expirationDate);
            response.setResponseData(authData);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setResponseData(null);
            Exception ex = new Exception("Neispravno korisničko ime ili lozinka.");
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

}
