/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.libraryback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Dragana Stefanovic
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "book_rent"
)
public class BookRent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rent_date")
    private LocalDate rentDate;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @ManyToOne(


    )
    @JoinColumn(
            name = "book",
            referencedColumnName = "id"

    )
    private Book book;
    @ManyToOne(

    )
    @JoinColumn(
            name = "library_member",
            referencedColumnName = "id"
    )
    private LibraryMember byMember;

}
