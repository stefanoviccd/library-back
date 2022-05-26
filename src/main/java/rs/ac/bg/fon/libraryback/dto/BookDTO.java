package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.BookGenre;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String ISBN;
    private String title;
    private int issueYear;
    private Author author;
    private BookGenre genre;
    private boolean isCurrentlyRented;
}
