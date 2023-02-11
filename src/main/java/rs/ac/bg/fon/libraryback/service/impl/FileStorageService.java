package rs.ac.bg.fon.libraryback.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.ProfilePicture;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IFileRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.service.IFileStorageService;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class FileStorageService implements IFileStorageService {
    @Autowired
    private IFileRepository fileRepository;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public ProfilePicture save(MultipartFile file, Long userId) throws IOException, UserNotFoundException, ValidationException {
        try{
        User dbUser = userRepository.findById(userId).get();
        if(dbUser.getProfilePicture()!=null){
            try{
                ProfilePicture dbPicture= fileRepository.findById(dbUser.getProfilePicture().getId()).get();
                dbPicture.setData(file.getBytes());
                dbPicture.setName(StringUtils.cleanPath(file.getOriginalFilename()));
                dbPicture.setType(file.getContentType());
                fileRepository.save(dbPicture);
            }
            catch(Exception e){
                throw new ValidationException("User with invalid userId is sent!");
            }
        }
        else{
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            ProfilePicture FileDB = new ProfilePicture(fileName, file.getContentType(), file.getBytes());
            ProfilePicture saved=fileRepository.save(FileDB);
            dbUser.setProfilePicture(saved);
            userRepository.save(dbUser);
            return saved;
        }
        }
        catch (NoSuchElementException e){
            throw new UserNotFoundException("User with id "+userId+" does not exist!");
        }
        catch (ValidationException e){
            throw e;
        }
        return null;

    }
}
