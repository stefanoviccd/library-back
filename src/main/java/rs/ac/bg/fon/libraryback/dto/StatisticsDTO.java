package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticsDTO {
    private int bookNumber;
    private int titleNumber;
    private int userNumber;
    private int currentlyRentedBooksNumber;
}
