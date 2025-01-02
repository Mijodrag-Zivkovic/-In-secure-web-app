package com.News.News.mappers;

import com.News.News.dtos.AdminUserAccountRequest;
import com.News.News.dtos.UserAccountRequest;
import com.News.News.dtos.UserAccountResponse;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper {

    public static UserAccount toEntity(UserAccountRequest request) {
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Add encryption later!
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setIsEnabled(true); // Default to enabled
        return user;
    }

    public static UserAccountResponse toResponse(UserAccount user) {
        return new UserAccountResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().toString(),
                user.getIsEnabled()
        );
    }

    public static UserAccount toEntity(AdminUserAccountRequest request) {
        UserAccount user = new UserAccount();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // Add encryption later!
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setIsEnabled(true); // Default to enabled
        return user;
    }

}
