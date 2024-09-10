package ru.kata.spring.boot_security.demo.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Set<User> listUsers() {
        return new HashSet<> (entityManager
                .createQuery( "SELECT u FROM User u LEFT JOIN FETCH u.roles" )
                .getResultList());
    }

    @Override
    public void removeUserById(Long id) {
        entityManager.createQuery("DELETE User WHERE id = :id")
                .setParameter("id",id)
                .executeUpdate();
    }
    @Override
    public User findUser(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return entityManager.find(User.class, id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public User findUserByLogin(String login) throws NoResultException {
        try {
            return (User)entityManager
                    .createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.login = :login")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException noResultException) {
            System.out.println("User not found!");
            throw new UsernameNotFoundException("User not found!",noResultException);
        }
    }
    @Override
    public boolean ifDBEmpty(){
        return  entityManager.createQuery("select 1 from User").getResultList().isEmpty();
    }
}
