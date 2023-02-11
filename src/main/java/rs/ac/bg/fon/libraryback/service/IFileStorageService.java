package rs.ac.bg.fon.libraryback.service;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;

import java.io.IOException;


public interface IFileStorageService {
    ProfilePicture save(MultipartFile file, Long userId) throws IOException, UserNotFoundException, ValidationException;
}
