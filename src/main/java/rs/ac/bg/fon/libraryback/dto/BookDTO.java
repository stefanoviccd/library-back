package rs.ac.bg.fon.libraryback.dto;

import lombok.Data;
import rs.ac.bg.fon.libraryback.model.Author;
import rs.ac.bg.fon.libraryback.model.BookGenre;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
public class BookDTO {
    private Long id;
    private String ISBN;
    private String title;
    private int issueYear;
    private String authorsName;
    private String authorsLastName;
    private String genre;
    private boolean isCurrentlyRented;
}
