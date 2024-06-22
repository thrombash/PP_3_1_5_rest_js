package ru.kata.spring.boot_security.demo.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            if (user.getId() == 0) {
                userService.loadUserByUsername(user.getUsername());
            }
            else return;
        } catch (UsernameNotFoundException e) {
            return;
        }
        errors.rejectValue("username", "", String.format("Username '%s' already exists",
                user.getUsername()));

    }
}
