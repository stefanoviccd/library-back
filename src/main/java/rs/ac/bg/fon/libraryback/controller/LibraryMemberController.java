package rs.ac.bg.fon.libraryback.controller;


import org.apache.tomcat.jni.Library;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.bg.fon.libraryback.communication.Response;
import rs.ac.bg.fon.libraryback.dto.BookRentDTO;
import rs.ac.bg.fon.libraryback.dto.LibraryMemberDTO;
import rs.ac.bg.fon.libraryback.model.LibraryMember;
import rs.ac.bg.fon.libraryback.service.BookRentService;
import rs.ac.bg.fon.libraryback.service.LibraryMemberService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/librarianMember")
public class LibraryMemberController {
    @Autowired
    private LibraryMemberService memberService;
    @Autowired
    private BookRentService rentService;
    @Autowired
    private ModelMapper modelMapper;

    public LibraryMemberController() {
    }

    @GetMapping
    @CrossOrigin
    public ResponseEntity<Response> getAllMembers() {
        Response response = new Response();
        try {
            List<LibraryMemberDTO> members = memberService.getAllMembers().stream().map(lm->modelMapper.map(lm, LibraryMemberDTO.class)).collect(Collectors.toList());
            response.setResponseData(members);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/newCardNumber")
    @CrossOrigin
    public ResponseEntity<Response> getNewCardNumber() {
        Response response = new Response();
        try {
            String cardNumber = memberService.generateCardNumber();
            response.setResponseData(cardNumber);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("{id}/rents")
    @CrossOrigin
    public ResponseEntity<Response> getUserRents(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            List<BookRentDTO> rents = rentService.getUserRents(id).stream().map(r->modelMapper.map(r, BookRentDTO.class)).collect(Collectors.toList());
            response.setResponseData(rents);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/{value}")
    @CrossOrigin
    public ResponseEntity<Response> getMembersByValue(@PathVariable(name = "value") String value) {
        Response response = new Response();
        try {
            List<LibraryMemberDTO> members = memberService.getByValue(value).stream().map(m->modelMapper.map(m, LibraryMemberDTO.class)).collect(Collectors.toList());
            response.setResponseData(members);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/id/{id}")
    @CrossOrigin
    public ResponseEntity<Response> getMembersById(@PathVariable(name = "id") Long value) {
        Response response = new Response();
        try {
            LibraryMemberDTO dbMember = modelMapper.map(memberService.getById(value), LibraryMemberDTO.class);
            response.setResponseData(dbMember);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @GetMapping("/userCard/{cardNum}")
    @CrossOrigin
    public ResponseEntity<Response> getMemberByCardNumber(@PathVariable(name = "cardNum") String value) {
        Response response = new Response();
        try {
            LibraryMemberDTO dbMember = modelMapper.map(memberService.getByExactCardNumber(value), LibraryMemberDTO.class);
            System.out.println(dbMember);
            response.setResponseData(dbMember);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @CrossOrigin
    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteMember(@PathVariable(name = "id") Long id) {
        Response response = new Response();
        try {
            memberService.deleteMember(id);
            response.setResponseData(null);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);

        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            ex.printStackTrace();
            return ResponseEntity.ok().body(response);
        }

    }

    @PutMapping()
    @CrossOrigin
    public ResponseEntity<Response> updateMember(@RequestBody LibraryMemberDTO member) {

        Response response = new Response();
        try {
            LibraryMember transformedLibraryMemberObject=modelMapper.map(member, LibraryMember.class);
            LibraryMemberDTO dbMember = modelMapper.map(memberService.update(transformedLibraryMemberObject), LibraryMemberDTO.class);
            response.setResponseData(dbMember);
            response.setResponseException(null);
            return ResponseEntity.status(HttpStatus.OK).body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<Response> saveMember(@RequestBody LibraryMemberDTO member) {
        Response response = new Response();
        try {
            LibraryMember transformedLibraryMemberObject=modelMapper.map(member, LibraryMember.class);
            LibraryMemberDTO save = modelMapper.map(memberService.save(transformedLibraryMemberObject), LibraryMemberDTO.class);
            response.setResponseData(save);
            response.setResponseException(null);
            return ResponseEntity.ok().body(response);


        } catch (Exception ex) {
            response.setResponseData(null);
            response.setResponseException(ex);
            return ResponseEntity.ok().body(response);
        }

    }

}
