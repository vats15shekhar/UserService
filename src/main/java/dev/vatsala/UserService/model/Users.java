package dev.vatsala.UserService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity(name ="users")
@Getter
@Setter

public class Users extends BaseModel{

    private String email;
    private String password;

    @ManyToMany
    private Set<Role> roles = new HashSet<>();
}
