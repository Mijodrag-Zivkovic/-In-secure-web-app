package com.News.News.controllers;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getUserById(@PathVariable Long id) {
        AccountResponse response = accountService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<AccountResponse> getUserByUsername(@RequestParam String username) {
        AccountResponse response = accountService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createUser(@RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        accountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllUsers() {
        List<AccountResponse> responseList = accountService.getAllUsers();
        return ResponseEntity.ok(responseList);
    }

}
