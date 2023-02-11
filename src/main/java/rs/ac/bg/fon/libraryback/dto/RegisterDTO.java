package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String contact;
    private String username;
    private String password;
    private ProfilePicture profilePicture;
}
