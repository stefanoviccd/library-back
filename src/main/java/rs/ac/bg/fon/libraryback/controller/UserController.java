package rs.ac.bg.fon.libraryback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.el.util.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.constants.Constants;
import rs.ac.bg.fon.libraryback.dto.RegisterDTO;
import rs.ac.bg.fon.libraryback.dto.UserDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;
import rs.ac.bg.fon.libraryback.repository.BlacklistRepository;
import rs.ac.bg.fon.libraryback.service.IUserService;
import rs.ac.bg.fon.libraryback.service.impl.FileStorageService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("api/v1/")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private FileStorageService fileService;
    @GetMapping("refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

                try {
                    userService.refreshToken(request, response);

                } catch (Exception ex) {
                    response.setHeader("error", ex.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> errors = new HashMap<>();
                    errors.put("error_message", ex.getMessage());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);

                }





    }
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int offset,
                                           @RequestParam(required = false, defaultValue = "firstName") String sortAttribude,
                                           @RequestParam(required = false, defaultValue = "asc") String sortOrder){
        Response response=new Response();
        try{
        List<UserDTO> users=userService.getAll(page, offset, sortAttribude,sortOrder);
        response.setResponseData(users);
        response.setResponseException(null);
        return ResponseEntity.ok(response);
        }
        catch(Exception ex){
        response.setResponseException(ex.getMessage());
        response.setResponseData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        }
    }
    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getById(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            UserDTO dbMember = userService.getById(id);
            response.setResponseData(dbMember);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (ValidationException ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
        catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    }
    @GetMapping("/user/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> getByValue(@RequestParam(required = false, defaultValue = "") String value,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int offset,
                                               @RequestParam(required = false, defaultValue = "first_name") String sortAttribute,
                                               @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        Response response = new Response();
        try {

            List<UserDTO> users = userService.getByValue(value, page, offset, sortAttribute,sortOrder);
            response.setResponseData(users);
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

    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestBody RegisterDTO userDto){
        Response response=new Response();
        try{
            UserDTO saved=userService.saveUser(userDto);
            response.setResponseData(saved);
            response.setResponseException(null);
            return ResponseEntity.created(URI.create(Constants.SAVE_USER_URI)).body(response);
        }
        catch(ValidationException ex){
            response.setResponseException(ex.getMessage());
            response.setResponseData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        catch(Exception ex){
            response.setResponseException(ex.getMessage());
            response.setResponseData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> deleteUser(@PathVariable(name = "id") Long id){
        Response response=new Response();
        try{
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        catch(Exception ex){
            response.setResponseException(ex.getMessage());
            response.setResponseData(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/logoutSuccess")
    private ResponseEntity<Response>  logoutSuccess() {
        return ResponseEntity.ok(new Response("You are logged out!", null));
    }

    @GetMapping(value="/logout")
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = authorizationHeader.substring("Bearer ".length());
       BlacklistRepository br= new BlacklistRepository();
       br.saveToBlacklist(token);
        }
@PostMapping("/user/upload")
    public ResponseEntity<Response> uploadProfilePicture (@RequestParam("file") MultipartFile file, @RequestParam("id") Long userId) {
    Response response=new Response();
    try{
        ProfilePicture saved= fileService.save(file, userId);
        response.setResponseData(saved);
        response.setResponseException(null);
        return ResponseEntity.ok(response);
    }
    catch(ValidationException ex){
        response.setResponseException(ex.getMessage());
        response.setResponseData(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    catch(Exception ex){
        response.setResponseException(ex.getMessage());
        response.setResponseData(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    }
    @GetMapping("/newCardNumber")
    @CrossOrigin
    public ResponseEntity<Response> getNewCardNumber() {
        Response response = new Response();
        try {
            String cardNumber = userService.generateCardNumber();
            response.setResponseData(cardNumber);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);

        }
    }




}
