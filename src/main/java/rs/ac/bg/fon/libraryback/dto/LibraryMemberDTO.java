package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.fon.libraryback.model.MembershipCard;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryMemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String contact;
    private MembershipCard membershipCard;
}
