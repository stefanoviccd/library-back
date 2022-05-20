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
 *
 * @author Dragana Stefanovic
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "isbn")
    private String ISBN;
    private String title;

    @Column(name = "issue_year")
    private int issueYear;
    @ManyToOne(

    )
    @JoinColumn(
            name = "author_id",
            referencedColumnName = "id"
    )
    private Author author;
    private BookGenre genre;
    @Column(name = "is_currently_rented")
    private boolean isCurrentlyRented;
}
