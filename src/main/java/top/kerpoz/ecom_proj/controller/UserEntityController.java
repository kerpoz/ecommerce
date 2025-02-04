package top.kerpoz.ecom_proj.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.kerpoz.ecom_proj.model.UserEntity;
import top.kerpoz.ecom_proj.service.UserEntityService;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserEntity user) {
        userEntityService.register(user);
    }

    @GetMapping("/{userId}")
    public Optional<UserEntity> getUserById(@PathVariable Long userId) {
        return userEntityService.findUserById(userId);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {
        return userEntityService.verifyUser(user);
    }
}
