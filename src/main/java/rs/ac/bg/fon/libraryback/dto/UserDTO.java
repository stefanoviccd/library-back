package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.fon.libraryback.model.MembershipCard;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;
import rs.ac.bg.fon.libraryback.model.Role;

import java.io.Serializable;
import java.lang.reflect.Member;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String contact;
    private String status;
    private MembershipCard card;
    private ProfilePictureDTO profilePicture;
}
