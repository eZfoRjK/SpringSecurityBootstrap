package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // Отображение всех пользователей
    @GetMapping
    public String printUsers(ModelMap model) {
        model.addAttribute( "allUsers", userService.listUsers() );
        return "admin";
    }

    // Удаление пользователя
    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name = "id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    // Переход на страницу добавления нового пользователя
    @GetMapping("/add")
    public String newUser(@ModelAttribute("user") User user, ModelMap modelRole) {
        modelRole.addAttribute("modelRole", roleService.rolesSet());
        return "admin";
    }


    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                try {
                    Long id = Long.parseLong(role.getRole());
                    Role roleFromDB = roleService.findRole(id);
                    if (roleFromDB != null) {
                        roles.add(roleFromDB);
                    } else {
                        System.out.println("Роль с id " + id + " не найдена");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка преобразования роли: " + role.getRole());
                }
            }
        }
        user.setRoles(roles);
        userService.add(user);
        return "redirect:/admin";
    }

//    @PostMapping("/add")
//    public String addUser(@ModelAttribute("user") User user,
//                          @RequestParam(required = false) List<Long> roleSelectedID) {
//        Set<Role> roles = new HashSet<>();
//            if (roleSelectedID != null) {
//                for (Long roleId : roleSelectedID) {
//                    Role roleFromDB = roleService.findRole(roleId);
//                    if (roleFromDB != null) {
//                        roles.add(roleFromDB);
//                    } else {
//                        System.out.println("Роль с id " + roleId + " не найдена");
//                    }
//                }
//            }
//    user.setRoles(roles);
//    userService.add(user);
//    return "redirect:/admin";
//    }

    // Переход на страницу редактирования пользователя
    @GetMapping("/edit")
    public String changeUser(@RequestParam(name = "id") Long id, ModelMap model) {
        model.addAttribute("user", userService.findUser(id));
        model.addAttribute( "modelRole", roleService.rolesSet());
        model.addAttribute( "roleSelect", new ArrayList<Long>());
        return "redirect:/admin";
    }

    // Обновление данных пользователя
    @PatchMapping("/change")
    public String update(@RequestParam(required = false) List<Long> roleSelectedID,
                         @ModelAttribute("user") User changedUser) {
        Set<Role> roles = new HashSet<>();
        if (roleSelectedID != null) {
            for (Long roleIdFromFront : roleSelectedID) {
                Role role = roleService.findRole(roleIdFromFront);
                if (role != null) {
                    roles.add(role);
                } else {
                    System.out.println("Роль с id " + roleIdFromFront + " не найдена");
                }
            }
        }
        changedUser.setRoles(roles);
        userService.update(changedUser);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String updateUser(@RequestParam List<Long> roleSelectedID, @ModelAttribute User changedUser) {
        Set<Role> roles = new HashSet<>();
        if (roleSelectedID != null) {
            for (Long roleId : roleSelectedID) {
                Role role = roleService.findRole(roleId);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        changedUser.setRoles(roles);
        userService.update(changedUser);
        return "redirect:/admin";
    }
}
