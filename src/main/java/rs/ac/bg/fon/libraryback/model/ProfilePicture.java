package rs.ac.bg.fon.libraryback.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="profile_picture")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] data;
    public ProfilePicture(String name, String type, byte[] data){
        this.type=type;
        this.name=name;
        this.data=data;
    }

}
