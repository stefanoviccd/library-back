package rs.ac.bg.fon.libraryback;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LibraryBackApplication {

	public static void main(String[] args) {

		SpringApplication.run(LibraryBackApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
	ModelMapper mapper=new ModelMapper();
		return new ModelMapper();
	}
	@Bean
	protected PasswordEncoder passwordEncoder() throws Exception {
		return new BCryptPasswordEncoder();
	}




}
