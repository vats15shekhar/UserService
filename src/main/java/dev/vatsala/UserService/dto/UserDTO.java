package dev.vatsala.UserService.dto;

import dev.vatsala.UserService.model.Role;
import dev.vatsala.UserService.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private String email;
    private Set<Role> roles = new HashSet<>();

    public static UserDTO from(Users user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
