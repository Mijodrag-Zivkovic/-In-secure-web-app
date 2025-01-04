package com.News.News.mappers;

import com.News.News.dtos.AdminAccountRequest;
import com.News.News.dtos.AdminAccountResponse;
import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static UserAccount toEntity(AccountRequest request) {
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Add encryption later!
        user.setEmail(request.getEmail());
        user.setRole(Role.READER);
        user.setIsEnabled(true); // Default to enabled
        return user;
    }

    public static AccountResponse toResponse(UserAccount user) {
        return new AccountResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                user.getIsEnabled()
        );
    }

    public static UserAccount toEntity(AdminAccountRequest request) {
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Add encryption later!
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setIsEnabled(true); // Default to enabled
        return user;
    }

    public static AdminAccountResponse toAdminResponse(UserAccount user) {
        return new AdminAccountResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                user.getIsEnabled()
        );
    }
}
