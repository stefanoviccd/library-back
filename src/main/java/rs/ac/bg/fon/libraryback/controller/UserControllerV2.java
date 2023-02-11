package rs.ac.bg.fon.libraryback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.RegisterDTO;
import rs.ac.bg.fon.libraryback.dto.UserDTO;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.service.IUserService;
import rs.ac.bg.fon.libraryback.service.impl.FileStorageService;

import java.net.URI;

@RestController
@RequestMapping("api/v2/")
public class UserControllerV2 {
    @Autowired
    private IUserService userService;
    @Autowired
    private FileStorageService fileService;
    @PostMapping("/register")
    public ResponseEntity<Response> saveUser(@RequestParam("FirstName") String firstName, @RequestParam("LastName") String lastName, @RequestParam("Contact") String contact, @RequestParam("Username") String username, @RequestParam("Password") String password, @RequestParam(name = "file", required = false) MultipartFile file){
        Response response=new Response();
        try{
            UserDTO saved;
            if(file==null){
                 saved=userService.saveUser(new RegisterDTO(firstName, lastName,
                         contact, username, password, null));
            }
            else{
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                saved=userService.saveUser(new RegisterDTO(firstName, lastName, contact, username,
                        password, new ProfilePicture(fileName, file.getContentType(), file.getBytes())));


            }
            response.setResponseData(saved);
            response.setResponseException(null);
            return ResponseEntity.created(URI.create("http://localhost/8089/api/v1/register")).body(response);
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
    @PatchMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Response> updateUser(@RequestParam(name="Id") Long id,@RequestParam(name="FirstName", required = false) String firstName, @RequestParam(name="LastName", required = false) String lastName, @RequestParam(name="Contact", required = false) String contact, @RequestParam(name = "Username", required = false) String username, @RequestParam(name="Password", required = false) String password,  @RequestParam(name="CardNumber", required = false) String cardNumber, @RequestParam(name = "file", required = false) MultipartFile file) {
        Response response = new Response();
        try {
            ProfilePicture picture=null;
            if(file!=null){
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                picture=new ProfilePicture(fileName, file.getContentType(), file.getBytes());
            }
            MembershipCard card=null;
            if(cardNumber!=null){
                card=new MembershipCard();
                card.setCardNumber(cardNumber);

            }
            User user=new User(id, firstName, lastName, contact, username, password, null,card, picture);
            UserDTO updated= userService.update(user);
            response.setResponseData(updated);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
}
