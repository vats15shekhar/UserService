package dev.vatsala.UserService.controller;

import dev.vatsala.UserService.dto.*;
import dev.vatsala.UserService.model.Session;
import dev.vatsala.UserService.model.SessionStatus;
import dev.vatsala.UserService.model.Users;
import dev.vatsala.UserService.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;

    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignUpRequestDto request)
    {
        UserDTO userDTO = authService.signup(request.getEmail(), request.getPassword()).getBody();
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO request)
    {
        UserDTO userDTO = authService.login(request.getEmail(), request.getPassword()).getBody();
        return ResponseEntity.ok(userDTO);
    }

 /*   @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO request) {
        return authService.logout(request.getToken(), request.getUserId());
    }*/

    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logout(@PathVariable("id") Long userId, @RequestHeader("token") String token) {
        return authService.logout(token, userId);
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(ValidateTokenRequestDto request) {
        SessionStatus sessionStatus = authService.validate(request.getToken(), request.getUserId());

        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }

    //below APIs are only for learning purposes, should not be present in actual systems
    @GetMapping("/session")
    public ResponseEntity<List<Session>> getAllSession(){
        return authService.getAllSessions();
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers(){
        return authService.getAllUsers();
    }


}
