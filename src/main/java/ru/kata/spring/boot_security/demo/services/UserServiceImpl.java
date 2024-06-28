package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public void addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        User userFromDB = userRepository.findById(user.getId()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        if (!userFromDB.getPassword().equals(user.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        } else {
            user.setPassword(userFromDB.getPassword());
        }
        user.setRoles(userFromDB.getRoles());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
    }
}
