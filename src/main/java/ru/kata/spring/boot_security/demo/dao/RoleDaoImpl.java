package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findRole(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id не может быть null");
        }
        return entityManager.find(Role.class, id);
    }

    @Override
    public Set<Role> rolesSet() {
        return new HashSet<>(entityManager.createQuery(
                "SELECT r FROM Role r").getResultList());
    }

    @Override
    public void add(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
    }

    @Override
    public Role findByName(String roleName) {
        try {
            return entityManager.createQuery(
                    "SELECT r FROM Role r WHERE r.role = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println("Роль с именем " + roleName + " не найдена.");
            return null;
        }
    }
}
