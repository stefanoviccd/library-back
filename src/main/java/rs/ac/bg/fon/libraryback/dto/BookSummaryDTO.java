package rs.ac.bg.fon.libraryback.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
public class BookSummaryDTO {
    private String title;
    private String author;
    private String genre;
    private BigInteger totalCount;
    private int currentlyRentedCount;
}
