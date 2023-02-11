package rs.ac.bg.fon.libraryback.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.bg.fon.libraryback.dto.RegisterDTO;
import rs.ac.bg.fon.libraryback.dto.UserDTO;
import rs.ac.bg.fon.libraryback.dto.mapper.EntityToDtoMapper;
import rs.ac.bg.fon.libraryback.exception.UserAlreadyExistsException;
import rs.ac.bg.fon.libraryback.exception.ValidationException;
import rs.ac.bg.fon.libraryback.model.*;
import rs.ac.bg.fon.libraryback.repository.*;
import rs.ac.bg.fon.libraryback.service.IUserService;
import rs.ac.bg.fon.libraryback.utility.JsonWebTokenUtility;
import rs.ac.bg.fon.libraryback.validation.impl.AddUserValidator;
import rs.ac.bg.fon.libraryback.validation.impl.DeleteUserValidator;
import rs.ac.bg.fon.libraryback.validation.impl.UpdateUserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED)
@Slf4j
public class UserService implements IUserService, UserDetailsService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IFileRepository fileRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IBookRentRepository rentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private IMembershipCardRepository membershipCardRepository;
    EntityToDtoMapper mapper=new EntityToDtoMapper();

    private JsonWebTokenUtility jsonWebTokenUtility= new JsonWebTokenUtility();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AddUserValidator addUserValidator;
    @Autowired
    private DeleteUserValidator deleteUserValidator;
    @Autowired
    private UpdateUserValidator updateUserValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user=userRepository.findByUsername(username);
            if(user==null){
                log.error("User with username {} not found.", username);
                throw new UsernameNotFoundException("User not found.");
            }
            else{
                log.info("User with username {} found.", username);
            }
            Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
            user.getRoles().forEach(role ->{
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getLocalizedMessage());
        }
    }

    @Override
    public User getByUsername(String username) {
        try{
            User dbUser=userRepository.findByUsername(username);
            if(dbUser==null)
                throw new UsernameNotFoundException("User with username "+username+" does not exist in database!");
            return dbUser;
        }
        catch(Exception e){
        throw e;
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());;
            String username = jsonWebTokenUtility.extractUsernameFromToken(token);
            User user=getByUsername(username);
            String accessToken = jsonWebTokenUtility.generateToken(user, request.getRequestURL().toString(), 10*60*1000);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", token);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
        else{
            throw new Exception("Refresh token is missing!");
        }

    }

    @Override
    public List<UserDTO> getAll(int page, int offset, String sortAttribute, String sortOrder) {
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
        List<User> users=userRepository.findAll(p).getContent();
        List<UserDTO> userDtos=new ArrayList<>();
        for (User user: users
             ) {
            UserDTO dto=mapper.toDto(user);
            userDtos.add(dto);
        }
        return userDtos;
    }

    @Override
    public UserDTO saveUser(RegisterDTO registerDTO) throws ValidationException, UserAlreadyExistsException {
        addUserValidator.validate(registerDTO);
        User user=new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setContact(registerDTO.getContact());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        Collection<Role> roles=new ArrayList<>();
        Role dbRole=roleRepository.findByName("ROLE_USER");
        roles.add(dbRole);
        user.setRoles(roles);
        if(registerDTO.getProfilePicture()!=null)
            user.setProfilePicture(registerDTO.getProfilePicture());
        MembershipCard card=new MembershipCard();
        card.setIssueDate(LocalDate.now());
        card.setExpiryDate(LocalDate.of(card.getIssueDate().getYear()+1, card.getIssueDate().getMonth(), card.getIssueDate().getDayOfMonth()));
        card.setCardNumber(generateCardNumber());
        membershipCardRepository.save(card);
        user.setMembershipCard(card);
        User saved=userRepository.save(user);
        return mapper.toDto(saved);
    }

    @Override
    public UserDTO getById(Long id) throws Exception {
        User dbResult;
        try {
            if (id == null) throw new ValidationException("Id value is not passed!");
            dbResult = userRepository.findById(id).get();
            return mapper.toDto(dbResult);
        } catch (NoSuchElementException e) {
            throw new Exception("User with id "+id+" does not exist in database!");
        }
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        try {
         deleteUserValidator.validate(id);
         User dbUser=userRepository.findById(id).get();
         List<BookRent> userRents= rentRepository.findByUser(dbUser.getId());
            rentRepository.deleteAll(userRents);
            userRepository.delete(dbUser);
            log.info("User with id "+id+" is deleted.");
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<UserDTO> getByValue(String value, int page, int offset, String sortAttribute, String sortOrder) throws ValidationException {
        if(page<0)
            throw new ValidationException("Invalid value for page number!");
        if(offset<0)
            throw new ValidationException("Invalid value for limit!");
        String searchValue="%"+value+"%";
        Sort sort;
        if(sortOrder.equalsIgnoreCase("desc"))
            sort=Sort.by(Sort.Direction.DESC, sortAttribute);
        else sort=Sort.by(Sort.Direction.ASC, sortAttribute);
        Pageable p = PageRequest.of(page, offset, sort);
        List<User> users = userRepository.findByValue(searchValue, p);
        if (users == null) {
            return new ArrayList<>();
        }
        List<UserDTO> dtos = new ArrayList<>();
        for (User u : users
        ) {
            dtos.add(mapper.toDto(u));
        }
        return dtos;

    }

    @Override
    public UserDTO update(User user) throws ValidationException, UserAlreadyExistsException {
        updateUserValidator.validate(user);
        try {
            User dbUser= userRepository.findById(user.getId()).get();
            if(user.getFirstName()!=null && !user.getFirstName().equals(dbUser.getFirstName())){
                dbUser.setFirstName(user.getFirstName());
            }
            if(user.getLastName()!=null && !user.getLastName().equals(dbUser.getLastName())){
                dbUser.setLastName(user.getLastName());
            }
            if(user.getContact()!=null && !user.getContact().equals(dbUser.getContact())){
                dbUser.setContact(user.getContact());
            }
            if(user.getUsername()!=null && !user.getUsername().equals(dbUser.getUsername())){
                dbUser.setUsername(user.getUsername());
            }
            if(user.getPassword()!=null && !dbUser.getPassword().equals(passwordEncoder.encode(user.getPassword()))){
                dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if(user.getProfilePicture()!=null){
                dbUser.setProfilePicture(user.getProfilePicture());
            }
            if(user.getMembershipCard()!=null){
                MembershipCard card=membershipCardRepository.getById(dbUser.getMembershipCard().getId());
                card.setIssueDate(LocalDate.now());
                card.setExpiryDate(LocalDate.of(card.getIssueDate().getYear()+1, card.getIssueDate().getMonth(), card.getIssueDate().getDayOfMonth()));
                card.setCardNumber(user.getMembershipCard().getCardNumber());
                membershipCardRepository.save(card);
            }


            userRepository.save(dbUser);
            return mapper.toDto(dbUser);
        }
        catch (NoSuchElementException e){
            throw new NoSuchElementException("User with id "+user.getId()+" does not exist in database!");
        }
        catch (Exception e){
            throw e;
        }
    }
    @Override
    public String generateCardNumber() {
        Random randomGenerator = new Random();
        String cardNumber = "";
        for (int i = 0; i < 15; i++)
            cardNumber += randomGenerator.nextInt(10);
        return cardNumber;
    }
}
