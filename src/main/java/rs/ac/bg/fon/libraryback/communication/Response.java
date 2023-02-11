package rs.ac.bg.fon.libraryback.communication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Object responseData;
    private String responseException;


}
