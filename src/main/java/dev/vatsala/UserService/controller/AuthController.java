package dev.vatsala.UserService.controller;

import dev.vatsala.UserService.dto.LogoutRequestDTO;
import dev.vatsala.UserService.dto.SignUpRequestDto;
import dev.vatsala.UserService.dto.UserDTO;
import dev.vatsala.UserService.dto.ValidateTokenRequestDto;
import dev.vatsala.UserService.model.SessionStatus;
import dev.vatsala.UserService.service.AuthService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<UserDTO> login(@RequestBody SignUpRequestDto request)
    {
        UserDTO userDTO = authService.signup(request.getEmail(), request.getPassword()).getBody();
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO request) {
        return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(ValidateTokenRequestDto request) {
        SessionStatus sessionStatus = authService.validate(request.getToken(), request.getUserId());

        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
}
