package ru.kata.spring.boot_security.demo.injectedUsers;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class InjectedUsers {
    private final UserService userService;
    private final RoleService roleService;

    public InjectedUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initMakeUsers() {
        Role admin = new Role("ROLE_ADMIN");
        Role user = new Role("ROLE_USER");

        roleService.addRole(user);
        roleService.addRole(admin);

        Set<Role> adminRoles = new HashSet<>();
        Set<Role> userRoles = new HashSet<>();

        Hibernate.initialize(admin);
        Hibernate.initialize(user);

        userRoles.add(roleService.findRoleById(1L));
        adminRoles.add(roleService.findRoleById(1L));
        adminRoles.add(roleService.findRoleById(2L));

        User user1 = new User("Alexey", "Borodin", "admin", "admin");
        User user2 = new User("Mariya", "Kovaleva", "user", "user");

        user1.setRoles(adminRoles);
        user2.setRoles(userRoles);

        userService.addUser(user1);
        userService.addUser(user2);

    }
}
