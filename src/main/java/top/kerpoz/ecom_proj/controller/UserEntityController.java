package top.kerpoz.ecom_proj.controller;

import org.springframework.web.bind.annotation.*;
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.service.UserEntityService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    private final UserEntityService userService;

    public UserEntityController(UserEntityService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody UserEntity user) {
        userService.register(user);
    }

    @GetMapping("/{userId}")
    public Optional<UserEntity> getUserById(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }
}
