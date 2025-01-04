package com.News.News.services;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createUser(AccountRequest request);
    AccountResponse getUserById(Long id);
    AccountResponse getUserByUsername(String username);
    List<AccountResponse> getAllUsers();
    void deleteUser();
    AccountResponse updateUser(AccountRequest request);
    List<AccountResponse> searchUser(String keyword);

}
