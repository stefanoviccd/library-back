package rs.ac.bg.fon.libraryback.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.libraryback.exception.UserNotFoundException;
import rs.ac.bg.fon.libraryback.model.Librarian;
import rs.ac.bg.fon.libraryback.repository.UserRepository;
import rs.ac.bg.fon.libraryback.repository.impl.UserRepositoryImpl;

import java.util.ArrayList;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    CustomUserDetailsService(){
      }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Librarian u=userRepository.findByUsername(username);
            return new User(u.getUsername(), u.getPassword(), new ArrayList<>());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
