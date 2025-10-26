package br.com.pix.simulator.psp.controller;

import br.com.pix.simulator.psp.dto.user.UserCreateRequest;
import br.com.pix.simulator.psp.dto.user.UserResponse;
import br.com.pix.simulator.psp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> searchUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.searchUserById(userId));
    }

}
