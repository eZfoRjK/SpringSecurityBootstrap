package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.Set;

public interface UserDao {
    void add(User user);
    Set<User> listUsers();
    void removeUserById(Long id);
    User findUser(Long id);
    void update(User user);
    User findUserByLogin(String login);
    boolean ifDBEmpty();
}
