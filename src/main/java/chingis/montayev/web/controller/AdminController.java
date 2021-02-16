package chingis.montayev.web.controller;

import chingis.montayev.web.model.Role;
import chingis.montayev.web.model.User;
import chingis.montayev.web.services.RoleService;
import chingis.montayev.web.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    @GetMapping
//    public String user(Principal principal, Model model) {
//        String name = principal.getName();
//        User admin = userService.getByName(name);
//        model.addAttribute("admin", admin);
//        model.addAttribute("roles", admin.getRoles());
//        return "users";
//    }

    @GetMapping()
    public String getAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("userRoles", user.getRoles());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", userService.getUserById(id).getRoles());

        return "userById";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("rolesNames", roleService.getAll());
        return "new";
    }

    @PostMapping("/users")
    public String create(@ModelAttribute("user") User user, @RequestParam(value = "rolesNames") String[] roles) {
        Set<Role> rolesSet = new HashSet<>();
        for (String roleName : roles) {
            rolesSet.add(roleService.getByName(roleName));
        }
        user.setRoles(rolesSet);
        userService.add(user);
        return "redirect:/admin";
    }

    @GetMapping("/users/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("rolesNames", roleService.getAll());
        return "edit";
    }

    @PostMapping("/users/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("user") User user,
                         @RequestParam(value = "userRoles") String[] roles)

    {
        Set<Role> rolesSet = new HashSet<>();
        for (String roleName : roles) {
            rolesSet.add(roleService.getByName(roleName));
        }
        user.setRoles(rolesSet);
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
//
//    @ModelAttribute("headerMessage")
//    public String header() {
//        return "Task springCrudSecurity-242";
//    }
}




