package rs.ac.bg.fon.libraryback.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipCardDTO {
    private Long id;
    private String cardNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
}
