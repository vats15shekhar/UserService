package dev.vatsala.UserService.service;

import dev.vatsala.UserService.dto.UserDTO;
import dev.vatsala.UserService.model.Session;
import dev.vatsala.UserService.model.SessionStatus;
import dev.vatsala.UserService.model.Users;
import dev.vatsala.UserService.repository.SessionRepository;
import dev.vatsala.UserService.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
@Getter
@Setter
public class AuthService {

    UserRepository userRepository;
    SessionRepository sessionRepository;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository)
    {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<UserDTO> signup(String email, String password)
    {
        Users user = new Users();
        user.setEmail(email);
        user.setPassword(password);

        userRepository.save(user);

        return ResponseEntity.ok(UserDTO.from(user));
    }

    public ResponseEntity<UserDTO> login(String email, String password) {
        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return null;
        }

        Users user = userOptional.get();

        if (!user.getPassword().equals(password)) {
            return null;
        }

        String token = RandomStringUtils.randomAlphanumeric(30);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDTO userDto = new UserDTO();

//        Map<String, String> headers = new HashMap<>();
//        headers.put(HttpHeaders.SET_COOKIE, token);

        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add(HttpHeaders.SET_COOKIE, "auth-token:" + token);

        ResponseEntity<UserDTO> response = new ResponseEntity<>(userDto, headers, HttpStatus.OK);
//      response.getHeaders().add(HttpHeaders.SET_COOKIE, token);

        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        Session session = sessionOptional.get();

        session.setSessionStatus(SessionStatus.ENDED);

        sessionRepository.save(session);

        return ResponseEntity.ok().build();
    }

    public SessionStatus validate(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (sessionOptional.isEmpty()) {
            return null;
        }

        return SessionStatus.ACTIVE;
    }


}
