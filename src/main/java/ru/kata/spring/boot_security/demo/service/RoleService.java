package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleService {
    Role findRole(Long id);
    Set<Role> rolesSet();
    void addRole(Role role);
}
