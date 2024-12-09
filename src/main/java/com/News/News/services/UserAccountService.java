package com.News.News.services;

import com.News.News.dtos.UserAccountRequest;
import com.News.News.dtos.UserAccountResponse;

import java.util.List;

public interface UserAccountService {

    UserAccountResponse createUser(UserAccountRequest request);
    UserAccountResponse getUserById(Long id);
    UserAccountResponse getUserByUsername(String username);
    List<UserAccountResponse> getAllUsers();
    void deleteUser(Long id);
    UserAccountResponse updateUser(Long id, UserAccountRequest request);

}
