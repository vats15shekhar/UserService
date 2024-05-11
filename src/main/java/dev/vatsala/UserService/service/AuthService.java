package dev.vatsala.UserService.service;

import dev.vatsala.UserService.dto.UserDTO;
import dev.vatsala.UserService.exception.InvalidCredentialException;
import dev.vatsala.UserService.exception.InvalidTokenException;
import dev.vatsala.UserService.exception.UserNotFoundException;
import dev.vatsala.UserService.model.Session;
import dev.vatsala.UserService.model.SessionStatus;
import dev.vatsala.UserService.model.Users;
import dev.vatsala.UserService.repository.SessionRepository;
import dev.vatsala.UserService.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;*/
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.http.ResponseCookie;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Service
@Getter
@Setter
public class AuthService {

    UserRepository userRepository;
    SessionRepository sessionRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserDTO> signup(String email, String password)
    {
        Users user = new Users();
        user.setEmail(email);
        // Now we are setting the password in the encoded form using the Bcrypt Password Encoder
        // We need the Spring Security dependency in order to use the Bcrypt password encoder

        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        return ResponseEntity.ok(UserDTO.from(user));
    }

    public ResponseEntity<UserDTO> login(String email, String password) {
        Optional<Users> userOptional = userRepository.findByEmail(email);


        // verify the user at the time of login
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }

        Users user = userOptional.get();

        // verify the password
        // Setting the password and checking using bcryptPasswordEncoder

        // We compare the password passed from the UI at the time of login, and the password saved by in the database at the time of signup
     /*   if (! bCryptPasswordEncoder.matches(user.getPassword(), password)) {
            throw new InvalidCredentialException("User password is incorrect");
        }*/

        /*  if (!user.getPassword().equals(password)) {
           throw new InvalidCredentialException("User password is incorrect");
        }*/

        // Generate a random token
        /*
        We do not want to generate tokens as random numbers. We will now see how jwt tokens are created
        String token = RandomStringUtils.randomAlphanumeric(30);
         */


        MacAlgorithm alg = Jwts.SIG.HS256; // HS256 algo added for JWT
        SecretKey key = alg.key().build(); // generating the secret key

        //start adding the claims. These are the data that we want to pass as a part of JWT tokens
        //so that the Resource Server does not have to hit the Auth Server again to check the roles of the users, expiry time and other info
        Map<String, Object> jsonForJWT = new HashMap<>();
        jsonForJWT.put("email", user.getEmail());
        jsonForJWT.put("roles", user.getRoles());
        jsonForJWT.put("createdAt", new Date());
        jsonForJWT.put("expiryAt", new Date(LocalDate.now().plusDays(3).toEpochDay()));

        // In this part we are building the token
        String token = Jwts.builder()
                .claims(jsonForJWT) // added the claims
                .signWith(key, alg) // added the algo and key
                .compact(); //building the token


        //Create a new session
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDTO userDto = new UserDTO();

      //  Map<String, String> headers = new HashMap<>(new HashMap<>());
      //  headers.put(HttpHeaders.SET_COOKIE, token);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
       // headers.addAll(HttpHeaders.SET_COOKIE, Collections.singletonList("auth-token:" + token));

      /*  ResponseEntity<UserDTO> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);
        response.getHeaders().add(HttpHeaders.SET_COOKIE, token);*/

        headers.add(HttpHeaders.SET_COOKIE, token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);
    }

    public ResponseEntity<Void> logout(String token, Long userId) {

        // validations -> token exists, token is not expired, user exists else throw an exception
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null; // Write an exception to handle this situation
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        //verifying from DB if session exists
        if (sessionOptional.isEmpty() || sessionOptional.get().getSessionStatus().equals(SessionStatus.ENDED)) {
            throw new InvalidTokenException("token is invalid");
        }

        return SessionStatus.ACTIVE;
    }

    public ResponseEntity<List<Session>> getAllSessions()
    {

        List<Session> session = sessionRepository.findAll();
        return ResponseEntity.ok(session);
    }

    public ResponseEntity<List<Users>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }


}

/*
    MultiValueMapAdapter is map with single key and multiple values
    Headers
    Key     Value
    Token   """
    Accept  application/json, text, images
 */
