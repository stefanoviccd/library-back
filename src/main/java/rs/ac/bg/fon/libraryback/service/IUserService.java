package rs.ac.bg.fon.libraryback.service;

import rs.ac.bg.fon.libraryback.dto.RegisterDTO;
import rs.ac.bg.fon.libraryback.dto.UserDTO;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService {
    User getByUsername(String username);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception;

    List<UserDTO> getAll(int page, int offset, String sortAttribude, String sortOrder);


    UserDTO saveUser(RegisterDTO userDto) throws ValidationException, UserAlreadyExistsException;

    UserDTO getById(Long id) throws Exception;

    void deleteUser(Long id) throws ValidationException, Exception;

    List<UserDTO> getByValue(String value, int page, int offset, String sortAttribute, String sortOrder) throws ValidationException;

    UserDTO update(User user) throws ValidationException, UserAlreadyExistsException;

    String generateCardNumber();
}
