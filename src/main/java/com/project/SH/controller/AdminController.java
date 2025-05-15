package com.project.SH.controller;

import com.project.SH.model.impl.User;
import com.project.SH.service.impl.AdminService;
import com.project.SH.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> findAllUser(){
        return service.findAllUsers();
    }

    @PostMapping("/save_User")
    public User saveUser(@RequestBody User user) {
        return service.saveUser(user);
    }

    @GetMapping("/get_User/{userID}")
    public User findByID(@PathVariable int userID) {
        return service.findByID(userID);
    }

    @PutMapping("update_User")
    public User updateUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("delete_User/{userID}")
    public void deleteUser(@PathVariable int userID) {
        service.deleteUser(userID);
    }
}