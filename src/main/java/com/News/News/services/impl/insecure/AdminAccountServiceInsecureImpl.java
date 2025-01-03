package com.News.News.services.impl.insecure;

import com.News.News.dtos.AdminAccountRequest;
import com.News.News.dtos.AdminAccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.AccountMapper;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.services.AdminAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Transactional
@Service
public class AdminAccountServiceInsecureImpl implements AdminAccountService {

    private final AccountRepository accountRepository;

    @Override
    public AdminAccountResponse createUser(AdminAccountRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", ErrorCode.CONFLICT);
        }

        // Directly use the password from the request (vulnerability)
        UserAccount user = AccountMapper.toEntity(request);
        UserAccount savedUser = accountRepository.save(user);
        return AccountMapper.toAdminResponse(savedUser);
    }

    @Override
    public AdminAccountResponse getUserById(Long id) {
        UserAccount user = accountRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));
        return AccountMapper.toAdminResponse(user);
    }

    @Override
    public AdminAccountResponse getUserByUsername(String username) {
        UserAccount user = accountRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with username: " + username, ErrorCode.NOT_FOUND));
        return AccountMapper.toAdminResponse(user);
    }

    @Override
    public List<AdminAccountResponse> getAllUsers() {
        List<UserAccount> users = accountRepository.findAll();
        return users.stream().map(AccountMapper::toAdminResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AdminAccountResponse updateUser(Long id, AdminAccountRequest request) {
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
        return AccountMapper.toAdminResponse(updatedUser);
    }
}
