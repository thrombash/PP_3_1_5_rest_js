package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Long id);

    void addUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User findByUsername(String username);

    User findById(Long id);

}
