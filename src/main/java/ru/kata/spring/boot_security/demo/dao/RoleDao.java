package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;

public interface RoleDao {
    Role findRole(Long id);
    Set<Role> rolesSet();
    void add(Role role);
    Role findByName(String roleName);
}
