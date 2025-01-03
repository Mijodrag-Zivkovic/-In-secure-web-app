package com.News.News.services;

import com.News.News.dtos.AdminAccountRequest;
import com.News.News.dtos.AdminAccountResponse;

import java.util.List;

public interface AdminAccountService {
    AdminAccountResponse createUser(AdminAccountRequest request);
    AdminAccountResponse getUserById(Long id);
    AdminAccountResponse getUserByUsername(String username);
    List<AdminAccountResponse> getAllUsers();
    void deleteUser(Long id);
    AdminAccountResponse updateUser(Long id, AdminAccountRequest request);
}
