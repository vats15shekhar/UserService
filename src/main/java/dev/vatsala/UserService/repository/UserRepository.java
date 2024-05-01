package dev.vatsala.UserService.repository;

import dev.vatsala.UserService.model.Role;
import dev.vatsala.UserService.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}
