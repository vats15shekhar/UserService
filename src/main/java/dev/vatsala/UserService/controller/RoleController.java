package dev.vatsala.UserService.controller;
import dev.vatsala.UserService.model.Role;
import dev.vatsala.UserService.service.RoleService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private RoleService roleService;

        public RoleController(RoleService roleService)
        {
            this.roleService = roleService;
        }

}
