package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.utils.UserValidator;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminUserController {
    private final UserService userService;
    private final UserValidator userValidator;

    public AdminUserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors())
            return "registration";
        userService.saveUser(userForm);
        return "redirect:/login";
    }

    @GetMapping("/user")
    public String getUserPage(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUsers", allUsers);
        return "all_users";
    }

    @GetMapping("/admin/add_new_user")
    public String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user_info";
    }

    @PostMapping("/admin/save_user")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "user_info";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/update")
    public String updateUser(@RequestParam("id") long id, Model model) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            model.addAttribute("user", user);
            return "user_info";
        } else throw new UsernameNotFoundException(String.format("Username with id '%s' not found", id));
    }

    @GetMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
