package dev.umang.userauthservice.controllers;

import dev.umang.userauthservice.dtos.UserDTO;
import dev.umang.userauthservice.models.User;
import dev.umang.userauthservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable("id") Long id){

        System.out.println("Call from product catalog service");

        User user = userService.getUserById(id);

        return user.convertToUserDTO();
    }
}
