package com.News.News;

import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.services.impl.insecure.AccountServiceInsecureImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AccountServiceTests {

    @MockBean
    private AccountRepository accountRepository;

    //@InjectMocks
    @Autowired
    private AccountServiceInsecureImpl userAccountService;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        // Setup a sample UserAccount for testing
        userAccount = new UserAccount("testuser", "password123", "test@example.com", Role.READER, true);

    }

    @Test
    void testGetUserById_UserNotFound() {
        // Given
        Long userId = 1L;
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.getUserById(userId));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testCreateUser_Success() {
        // Given
        AccountRequest request = new AccountRequest("newuser", "password123", "new@example.com", "READER");
        Mockito.when(accountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);

        // When
        AccountResponse response = userAccountService.createUser(request);

        // Then
        assertNotNull(response);
        assertEquals(userAccount.getUsername(), response.getUsername());
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        // Given
        AccountRequest request = new AccountRequest("testuser", "password123", "test@example.com", "READER");
        Mockito.when(accountRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(userAccount));

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.createUser(request));
        assertEquals("Username already exists", thrown.getMessage());
        assertEquals(ErrorCode.CONFLICT, thrown.getErrorCode());
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        Long userId = 1L;
        Mockito.when(accountRepository.existsById(userId)).thenReturn(true);

        // When
        userAccountService.deleteUser(userId);

        // Then
        Mockito.verify(accountRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Given
        Long userId = 1L;
        Mockito.when(accountRepository.existsById(userId)).thenReturn(false);

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.deleteUser(userId));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        Long userId = 1L;
        AccountRequest request = new AccountRequest("updateduser", "newpassword123", "updated@example.com", "ADMIN");
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        Mockito.when(accountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);

        // When
        AccountResponse response = userAccountService.updateUser(userId, request);

        // Then
        assertNotNull(response);
        assertEquals(userAccount.getUsername(), response.getUsername());
        assertEquals(userAccount.getEmail(), response.getEmail());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Given
        Long userId = 1L;
        AccountRequest request = new AccountRequest("updateduser", "newpassword123", "updated@example.com", "ADMIN");
        Mockito.when(accountRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.updateUser(userId, request));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testGetAllUsers_Success() {
        // Given
        List<UserAccount> users = List.of(userAccount);
        Mockito.when(accountRepository.findAll()).thenReturn(users);

        // When
        List<AccountResponse> responseList = userAccountService.getAllUsers();

        // Then
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(userAccount.getUsername(), responseList.get(0).getUsername());
    }


}
