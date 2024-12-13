package com.News.News.services.impl;

import com.News.News.dtos.UserAccountRequest;
import com.News.News.dtos.UserAccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.UserAccountMapper;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.UserAccountRepository;
import com.News.News.services.UserAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Profile("vulnerable")
public class UserAccountServiceInsecureImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserAccountResponse createUser(UserAccountRequest request) {
        if (userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (userAccountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", ErrorCode.CONFLICT);
        }

        // Directly use the password from the request (vulnerability)
        UserAccount user = UserAccountMapper.toEntity(request);
        UserAccount savedUser = userAccountRepository.save(user);
        return UserAccountMapper.toResponse(savedUser);
    }

    @Override
    public UserAccountResponse getUserById(Long id) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));
        return UserAccountMapper.toResponse(user);
    }

    @Override
    public UserAccountResponse getUserByUsername(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with username: " + username, ErrorCode.NOT_FOUND));
        return UserAccountMapper.toResponse(user);
    }

    @Override
    public List<UserAccountResponse> getAllUsers() {
        List<UserAccount> users = userAccountRepository.findAll();
        return users.stream().map(UserAccountMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (!userAccountRepository.existsById(id)) {
            throw new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND);
        }
        userAccountRepository.deleteById(id);
    }

    @Override
    public UserAccountResponse updateUser(Long id, UserAccountRequest request) {
        UserAccount user = userAccountRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));

        // Validate and update fields
        if (!user.getUsername().equals(request.getUsername()) &&
                userAccountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (!user.getEmail().equals(request.getEmail()) &&
                userAccountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }

        // Directly use the new password from the request (vulnerability)
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        UserAccount updatedUser = userAccountRepository.save(user);
        return UserAccountMapper.toResponse(updatedUser);
    }
}
