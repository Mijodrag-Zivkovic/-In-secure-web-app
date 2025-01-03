package com.News.News.services.impl;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.AccountMapper;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.services.AccountService;
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
public class AccountServiceInsecureImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createUser(AccountRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", ErrorCode.CONFLICT);
        }

        // Directly use the password from the request (vulnerability)
        UserAccount user = AccountMapper.toEntity(request);
        UserAccount savedUser = accountRepository.save(user);
        return AccountMapper.toResponse(savedUser);
    }

    @Override
    public AccountResponse getUserById(Long id) {
        UserAccount user = accountRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));
        return AccountMapper.toResponse(user);
    }

    @Override
    public AccountResponse getUserByUsername(String username) {
        UserAccount user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with username: " + username, ErrorCode.NOT_FOUND));
        return AccountMapper.toResponse(user);
    }

    @Override
    public List<AccountResponse> getAllUsers() {
        List<UserAccount> users = accountRepository.findAll();
        return users.stream().map(AccountMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AccountResponse updateUser(Long id, AccountRequest request) {
        UserAccount user = accountRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));

        // Validate and update fields
        if (!user.getUsername().equals(request.getUsername()) &&
                accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (!user.getEmail().equals(request.getEmail()) &&
                accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", ErrorCode.CONFLICT);
        }

        // Directly use the new password from the request (vulnerability)
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        UserAccount updatedUser = accountRepository.save(user);
        return AccountMapper.toResponse(updatedUser);
    }
}
