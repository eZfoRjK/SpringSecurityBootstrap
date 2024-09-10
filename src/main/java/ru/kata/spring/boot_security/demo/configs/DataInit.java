package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

@Component
public class DataInit implements ApplicationListener<ContextRefreshedEvent> {
    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInit(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userDao.ifDBEmpty()) {
            User admin = new User("admin", "admin", 44);
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setLogin("admin");

            Role adminRole = roleDao.findByName("ROLE_admin");
            if (adminRole == null) {
                adminRole = new Role("ROLE_admin");
                roleDao.add(adminRole);
            }
            admin.getRoles().add(adminRole);
            userDao.add(admin);
        }
    }
}
