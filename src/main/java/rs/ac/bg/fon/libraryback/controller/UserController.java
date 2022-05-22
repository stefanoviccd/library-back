package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.auth.CustomAuthenticationManager;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.service.CustomUserDetailsService;
import rs.ac.bg.fon.libraryback.service.UserService;
import rs.ac.bg.fon.libraryback.utility.JWTUtility;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private JWTUtility jwtUtility;

    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private UserService userService;

    public UserController() {
        userService = new UserService();
        authenticationManager=new CustomAuthenticationManager();
    }


    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<Response> login(@RequestBody Librarian user) {

        Librarian dbLibrarian = null;
        Response response = new Response();
        try {
           /* dbLibrarian = userService.login(user.getUsername(), user.getPassword());
            response.setResponseData(dbLibrarian);
            response.setResponseException(null);
            return ResponseEntity.ok(response);*/
           Authentication auth= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            final UserDetails userDetails=userDetailsService.loadUserByUsername(user.getUsername());
            String token= jwtUtility.generateToken(userDetails);
            response.setResponseData(token);
            return  ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setResponseData(null);
            response.setResponseException(e);
            e.printStackTrace();
            return ResponseEntity.ok(response);
        }

    }

}
