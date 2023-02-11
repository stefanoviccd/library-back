package rs.ac.bg.fon.libraryback.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.libraryback.dto.RentedBookInfo;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.dto.mapper.EntityToDtoMapper;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.Book;
import rs.ac.bg.fon.libraryback.model.BookRent;
import rs.ac.bg.fon.libraryback.model.User;
import rs.ac.bg.fon.libraryback.repository.IBookRentRepository;
import rs.ac.bg.fon.libraryback.repository.IBookRepository;
import rs.ac.bg.fon.libraryback.repository.IUserRepository;
import rs.ac.bg.fon.libraryback.service.IRentService;
import rs.ac.bg.fon.libraryback.validation.impl.RentBookValidator;
import rs.ac.bg.fon.libraryback.validation.impl.RestoreBookValidator;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RentService implements IRentService {
    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IBookRentRepository rentRepository;
    @Autowired
    private RentBookValidator rentBookValidator;
    @Autowired
    private RestoreBookValidator restoreBookValidator;
    private EntityToDtoMapper mapper=new EntityToDtoMapper();
    public RentedBookInfo rentBook(BookRentDTO rentDTO) throws ValidationException, UserAlreadyExistsException {
        rentBookValidator.validate(rentDTO);
        BookRent rent=new BookRent();
        Book dbBook= bookRepository.findById(rentDTO.getBookId()).get();
        User dbUser=userRepository.findById(rentDTO.getUserId()).get();
        rent.setUser(dbUser);
        rent.setBook(dbBook);
        rent.setRentDate(LocalDate.now());
        rent.setReturnDate(null);
        rentRepository.save(rent);
        dbBook.setCurrentlyRented(true);
        RentedBookInfo rentInfo=new RentedBookInfo();
        rentInfo.setBook(mapper.toDto(dbBook));
        rentInfo.setUser(mapper.toDto(dbUser));
        rentInfo.setIssueDate(Date.valueOf(LocalDate.now()));
        return rentInfo; }


    public RentedBookInfo restoreBook(BookRentDTO rentDTO) throws ValidationException, UserAlreadyExistsException {
        restoreBookValidator.validate(rentDTO);
        BookRent rent=rentRepository.findByUserAndBook(rentDTO.getUserId(), rentDTO.getBookId());
        Book dbBook= bookRepository.findById(rentDTO.getBookId()).get();
        User dbUser= userRepository.findById(rentDTO.getUserId()).get();
        if(rent==null){
            throw new ValidationException("Active rent for user "+ dbUser.getFirstName()+" "+dbUser.getLastName()+" and book "+dbBook.getTitle()+" does not exist!");
        }
        rent.setReturnDate(LocalDate.now());
        rentRepository.save(rent);
        dbBook.setCurrentlyRented(false);
        bookRepository.save(dbBook);
        RentedBookInfo rentInfo=new RentedBookInfo();
        rentInfo.setBook(mapper.toDto(dbBook));
        rentInfo.setUser(mapper.toDto(dbUser));
        rentInfo.setReturnDate(Date.valueOf(LocalDate.now()));
        rentInfo.setIssueDate(Date.valueOf(rent.getRentDate()));
        return rentInfo;
    }

    @Override
    public List<RentedBookInfo> getUserRents(Long userId, int page, int offset, String sortAttribute, String sortOrder) throws ValidationException {
        if(page<0)
            throw new ValidationException("Invalid value for page number!");
        if(offset<0)
            throw new ValidationException("Invalid value for limit!");
        if(userId<0){
            throw new ValidationException("Invalid value for user id!");
        }
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
        List<BookRent> dbRents=rentRepository.findByUser(userId, p);
        List<RentedBookInfo> rentDtos=new ArrayList<>();
        if(dbRents==null) return rentDtos;
        for (BookRent rent : dbRents
             ) {
            rentDtos.add(mapper.toDto(rent));
        }
        return rentDtos;

    }
}
