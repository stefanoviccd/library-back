package rs.ac.bg.fon.libraryback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {
    private int bookNumber;
    private int titleNumber;
    private int userNumber;
    private int currentlyRentedBooksNumber;
}
