package dev.vatsala.UserService.repository;

import dev.vatsala.UserService.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
