package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "user")
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String showUserPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "user";
    }

    @GetMapping("/{id}")
    public String findUserById(@PathVariable Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/{id}/edit-info")
    public String editUserInfo(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("allRoles", roleRepository.findAll());
        return "edit_self";
    }

    @PatchMapping("/{id}/edit-info")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "edit_self";
        }
        userService.updateUser(user);
        return "redirect:/user/" + id;
    }
}
