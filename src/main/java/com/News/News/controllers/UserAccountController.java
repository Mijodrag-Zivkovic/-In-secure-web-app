package com.News.News.controllers;

import com.News.News.dtos.UserAccountRequest;
import com.News.News.dtos.UserAccountResponse;
import com.News.News.services.UserAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class UserAccountController {

    private final UserAccountService userAccountService;


    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAccountResponse> getUserById(@PathVariable Long id) {
        UserAccountResponse response = userAccountService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<UserAccountResponse> getUserByUsername(@RequestParam String username) {
        UserAccountResponse response = userAccountService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserAccountResponse> createUser(@RequestBody @Valid UserAccountRequest request) {
        UserAccountResponse response = userAccountService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAccountResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserAccountRequest request) {
        UserAccountResponse response = userAccountService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userAccountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserAccountResponse>> getAllUsers() {
        List<UserAccountResponse> responseList = userAccountService.getAllUsers();
        return ResponseEntity.ok(responseList);
    }

}
