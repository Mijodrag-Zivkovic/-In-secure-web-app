package com.News.News;

import com.News.News.dtos.UserAccountRequest;
import com.News.News.dtos.UserAccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.UserAccountRepository;
import com.News.News.services.impl.UserAccountServiceInsecureImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserAccountServiceTests {

    @MockBean
    private UserAccountRepository userAccountRepository;

    //@InjectMocks
    @Autowired
    private UserAccountServiceInsecureImpl userAccountService;

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
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.getUserById(userId));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testCreateUser_Success() {
        // Given
        UserAccountRequest request = new UserAccountRequest("newuser", "password123", "new@example.com", "READER");
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);

        // When
        UserAccountResponse response = userAccountService.createUser(request);

        // Then
        assertNotNull(response);
        assertEquals(userAccount.getUsername(), response.getUsername());
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        // Given
        UserAccountRequest request = new UserAccountRequest("testuser", "password123", "test@example.com", "READER");
        Mockito.when(userAccountRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(userAccount));

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.createUser(request));
        assertEquals("Username already exists", thrown.getMessage());
        assertEquals(ErrorCode.CONFLICT, thrown.getErrorCode());
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        Long userId = 1L;
        Mockito.when(userAccountRepository.existsById(userId)).thenReturn(true);

        // When
        userAccountService.deleteUser(userId);

        // Then
        Mockito.verify(userAccountRepository).deleteById(userId);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        // Given
        Long userId = 1L;
        Mockito.when(userAccountRepository.existsById(userId)).thenReturn(false);

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.deleteUser(userId));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        Long userId = 1L;
        UserAccountRequest request = new UserAccountRequest("updateduser", "newpassword123", "updated@example.com", "ADMIN");
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        Mockito.when(userAccountRepository.save(Mockito.any(UserAccount.class))).thenReturn(userAccount);

        // When
        UserAccountResponse response = userAccountService.updateUser(userId, request);

        // Then
        assertNotNull(response);
        assertEquals(userAccount.getUsername(), response.getUsername());
        assertEquals(userAccount.getEmail(), response.getEmail());
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Given
        Long userId = 1L;
        UserAccountRequest request = new UserAccountRequest("updateduser", "newpassword123", "updated@example.com", "ADMIN");
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> userAccountService.updateUser(userId, request));
        assertEquals("User not found with ID: " + userId, thrown.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testGetAllUsers_Success() {
        // Given
        List<UserAccount> users = List.of(userAccount);
        Mockito.when(userAccountRepository.findAll()).thenReturn(users);

        // When
        List<UserAccountResponse> responseList = userAccountService.getAllUsers();

        // Then
        assertNotNull(responseList);
        assertEquals(1, responseList.size());
        assertEquals(userAccount.getUsername(), responseList.get(0).getUsername());
    }


}
