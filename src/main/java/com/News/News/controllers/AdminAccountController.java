package com.News.News.controllers;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.dtos.AdminAccountRequest;
import com.News.News.dtos.AdminAccountResponse;
import com.News.News.services.AccountService;
import com.News.News.services.AdminAccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAccountController {

    private final AdminAccountService accountService;


    public AdminAccountController(AdminAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminAccountResponse> getUserById(@PathVariable Long id) {
        AdminAccountResponse response = accountService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<AdminAccountResponse> getUserByUsername(@RequestParam String username) {
        AdminAccountResponse response = accountService.getUserByUsername(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AdminAccountResponse> createUser(@RequestBody @Valid AdminAccountRequest request) {
        AdminAccountResponse response = accountService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminAccountResponse> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid AdminAccountRequest request) {
        AdminAccountResponse response = accountService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        accountService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminAccountResponse>> getAllUsers() {
        List<AdminAccountResponse> responseList = accountService.getAllUsers();
        return ResponseEntity.ok(responseList);
    }

}
