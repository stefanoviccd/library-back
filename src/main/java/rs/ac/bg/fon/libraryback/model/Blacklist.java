package rs.ac.bg.fon.libraryback.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Entity
@Table(name = "blacklist")
@Data
public class Blacklist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    @Column(name = "token_timestamp")
    Timestamp timestamp;
}
