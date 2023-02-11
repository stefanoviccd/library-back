package rs.ac.bg.fon.libraryback.dto.mapper;

import org.springframework.context.annotation.Profile;
import rs.ac.bg.fon.libraryback.dto.BookDTO;
import rs.ac.bg.fon.libraryback.dto.ProfilePictureDTO;
import rs.ac.bg.fon.libraryback.dto.RentedBookInfo;
import rs.ac.bg.fon.libraryback.dto.UserDTO;
import rs.ac.bg.fon.libraryback.model.*;

import java.sql.Date;

public class EntityToDtoMapper {
    public UserDTO toDto(User user){
        UserDTO dto=new UserDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName((user.getLastName()));
        dto.setContact(user.getContact());
        dto.setId(user.getId());
        dto.setCard(user.getMembershipCard());
        if(doesContainRole(user, "ROLE_ADMIN"))
        {
            dto.setStatus("Admin");
        }
        else if( doesContainRole(user, "ROLE_USER")){
            dto.setStatus("User");
        }
        else dto.setStatus("Undefined");
        dto.setProfilePicture(toDto(user.getProfilePicture()));
        return dto;

    }
    public BookDTO toDto(Book book){
        BookDTO dto=new BookDTO();
        dto.setId(book.getId());
        dto.setISBN(book.getISBN());
        dto.setTitle(book.getTitle());
        dto.setCurrentlyRented(book.isCurrentlyRented());
        dto.setIssueYear(book.getIssueYear());
        dto.setAuthorsName(book.getAuthor().getName());
        dto.setAuthorsLastName(book.getAuthor().getLastName());
        dto.setGenre(book.getGenre().getName());
        return dto;

    }
    private  boolean doesContainRole(User user, String roleName){
        if(user.getRoles()==null) return false;
        for (Role r:user.getRoles()
        ) {
            if(r.getName().equalsIgnoreCase(roleName)) return true;
        }
        return false;
    }

    public RentedBookInfo toDto(BookRent rent) {
        RentedBookInfo dto=new RentedBookInfo();
        dto.setBook(toDto(rent.getBook()));
        dto.setUser(toDto(rent.getUser()));
        if(rent.getRentDate()!=null)
        dto.setIssueDate(Date.valueOf(rent.getRentDate()));
        if(rent.getReturnDate()!=null)
        dto.setReturnDate(Date.valueOf(rent.getReturnDate()));
        return dto;
    }

    public ProfilePictureDTO toDto(ProfilePicture p) {
        if(p==null) return null;
        ProfilePictureDTO dto=new ProfilePictureDTO();
        dto.setData(p.getData());
        dto.setType(p.getType());
        dto.setSize_in_Kilobytes(p.getData().length/100);
        return dto;
    }

}
