package rs.ac.bg.fon.libraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePictureDTO {
    private String type;
    private byte[] data;
    private double size_in_Kilobytes;
}
