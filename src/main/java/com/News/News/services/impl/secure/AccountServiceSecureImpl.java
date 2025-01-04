package com.News.News.services.impl.secure;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.AccountMapper;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.security.model.CustomAuthenticationToken;
import com.News.News.services.AccountService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Profile("secure")
public class AccountServiceSecureImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EntityManager entityManager;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public AccountResponse createUser(AccountRequest request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException("Username already exists", ErrorCode.CONFLICT);
        }
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException("Email already exists", ErrorCode.CONFLICT);
        }

        // Directly use the password from the request (vulnerability)
        request.setPassword(passwordEncoder.encode(request.getPassword()));
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
    public void deleteUser() {
        Long id = getUserId();
//        UserAccount userAccount = accountRepository.findById(id)
//                .orElseThrow(() -> new AppException("User not found with ID: " + id, ErrorCode.NOT_FOUND));
        accountRepository.deleteById(id);
    }

    @Override
    public AccountResponse updateUser(AccountRequest request) {
        Long id = getUserId();
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

        UserAccount updatedUser = accountRepository.save(user);
        return AccountMapper.toResponse(updatedUser);
    }

    @Override
    public List<AccountResponse> searchUser(String keyword) {
//        String query = "SELECT * FROM user_accounts WHERE username LIKE '%" + keyword + "%' and role = 'READER'";
//        System.out.println(keyword);
//        System.out.println(query);
//        List<UserAccount> foundArticles = entityManager.createNativeQuery(query, UserAccount.class).getResultList();
//
//        List<AccountResponse> accounts = new ArrayList<>();
//        try{
//            accounts= foundArticles.stream()
//                    .map(AccountMapper::toResponse)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//
//        return accounts;
        List<UserAccount> users = accountRepository.searchAccounts(keyword);
        //System.out.println(users);
        return users.stream().map(AccountMapper::toResponse).collect(Collectors.toList());
    }


    Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomAuthenticationToken) authentication).getUserId();
        return userId;
    }
}
