package dev.vatsala.UserService.repository;

import dev.vatsala.UserService.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    HashSet<Role> findAllByIdIn(List<Long> roleIds);
}
