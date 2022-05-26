package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.LibraryMember;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookRentDTO {
    private Long id;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private Book book;
    private LibraryMember byMember;

}
