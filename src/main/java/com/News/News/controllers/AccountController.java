package com.News.News.controllers;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.dtos.ArticleResponse;
import com.News.News.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/{id}")
//    public ResponseEntity<AccountResponse> getUserById(@PathVariable Long id) {
//        AccountResponse response = accountService.getUserById(id);
//        return ResponseEntity.ok(response);
//    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/search")
//    public ResponseEntity<AccountResponse> getUserByUsername(@RequestParam String username) {
//        AccountResponse response = accountService.getUserByUsername(username);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping
    public ResponseEntity<AccountResponse> createUser(@RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping()
    public ResponseEntity<AccountResponse> updateUser(
            @RequestBody @Valid AccountRequest request) {
        AccountResponse response = accountService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping()
    public ResponseEntity<Void> deleteUser() {
        accountService.deleteUser();
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<List<AccountResponse>> searchUsers(@RequestParam String keyword) {
        List<AccountResponse> articles = accountService.searchUser(keyword);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/passwordrecovery")
    public ResponseEntity<String> recoverPassword(String username){
        //account service call that would take email for username and send recovery email
        return ResponseEntity.ok("Recovery email sent successfully");
    }
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping
//    public ResponseEntity<List<AccountResponse>> getAllUsers() {
//        List<AccountResponse> responseList = accountService.getAllUsers();
//        return ResponseEntity.ok(responseList);
//    }

}
