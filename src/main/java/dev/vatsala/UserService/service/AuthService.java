package dev.vatsala.UserService.service;

import dev.vatsala.UserService.dto.UserDTO;
import dev.vatsala.UserService.model.Users;
import dev.vatsala.UserService.repository.SessionRepository;
import dev.vatsala.UserService.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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


}
