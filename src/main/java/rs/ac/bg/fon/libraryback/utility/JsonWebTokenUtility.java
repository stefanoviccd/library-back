package rs.ac.bg.fon.libraryback.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import rs.ac.bg.fon.libraryback.constants.Constants;
import rs.ac.bg.fon.libraryback.model.Blacklist;
import rs.ac.bg.fon.libraryback.model.Role;
import rs.ac.bg.fon.libraryback.repository.BlacklistRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JsonWebTokenUtility {
    @Autowired
    private BlacklistRepository blackListRepository=new BlacklistRepository();

    private static String secret= Constants.SECRET;
    private static  Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());


    public  String generateToken(User user, String issuer,  int expirationMiliseconds){
        return JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + expirationMiliseconds)).withIssuer(issuer).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);

    }
    public  String generateToken(rs.ac.bg.fon.libraryback.model.User user, String issuer, int expirationMiliseconds){
       return JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + expirationMiliseconds)).withIssuer(issuer)
               .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
               .sign(algorithm);
    }
    public  UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String authorizationHeader, String username) {
        String token = authorizationHeader.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        if(isBlackListed(token))
            throw new JWTVerificationException("Token is blacklisted!");
        String[] roles = decoded.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        //passport is not needed here because user is already authenticated
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        return usernamePasswordAuthenticationToken;
    }

    private boolean isBlackListed(String token) {
        Blacklist dbToken= blackListRepository.getByToken(token);
        return dbToken!=null && dbToken.getTimestamp().before(Timestamp.valueOf(LocalDateTime.now()));

    }

    public String extractUsernameFromHeader(String authorizationHeader){
        String token = authorizationHeader.substring("Bearer ".length());
        return extractUsernameFromToken(token);
    }
    public String extractUsernameFromToken(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);
        return decoded.getSubject();
    }

}
