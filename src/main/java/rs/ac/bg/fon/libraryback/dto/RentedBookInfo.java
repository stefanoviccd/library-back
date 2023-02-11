package rs.ac.bg.fon.libraryback.dto;

import lombok.Data;

import java.util.Date;
@Data
public class RentedBookInfo {
    BookDTO book;
    UserDTO user;
    Date issueDate;
    Date returnDate;
}
