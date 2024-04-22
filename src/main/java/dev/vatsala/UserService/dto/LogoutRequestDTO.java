package dev.vatsala.UserService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequestDTO {

    private long userId;
    private String token;
}
