/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.ac.bg.fon.libraryback.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.util.Collection;

/**
 *
 * @author Dragana Stefanovic
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String contact;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id",
            referencedColumnName = "id")
    private MembershipCard membershipCard;
    @OneToOne(fetch = FetchType.EAGER, cascade ={CascadeType.REMOVE, CascadeType.PERSIST})
    @JoinColumn(name = "picture_id",
            referencedColumnName = "id")
    @Nullable
    private ProfilePicture profilePicture;
    
}
