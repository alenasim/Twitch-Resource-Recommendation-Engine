package com.laioffer.twitch.user;

import com.laioffer.twitch.model.RegisterBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    // @RestController 的 dependency是 UserService.用dependency把它传进来。
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK) // 告诉request成功的话，return ok
    public void register(@RequestBody RegisterBody body) {
        userService.register(body.username(), body.password(), body.firstName(), body.lastName());
    }
}
