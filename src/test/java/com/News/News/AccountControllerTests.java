package com.News.News;

import com.News.News.controllers.AccountController;
import com.News.News.dtos.AccountRequest;
import com.News.News.dtos.AccountResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.services.AccountService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUserById_Success() throws Exception {
        // Given
        Long userId = 1L;
        AccountResponse response = new AccountResponse(1L, "john_doe", "john@example.com", "ADMIN", true);
        Mockito.when(accountService.getUserById(userId)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/accounts/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.id").value(1L))
                .andExpect( jsonPath("$.username").value("john_doe"))
                .andExpect( jsonPath("$.email").value("john@example.com"))
                .andExpect( jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        // Given
        Long userId = 1L;
        Mockito.when(accountService.getUserById(userId)).thenThrow(new AppException("User not found", ErrorCode.NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/accounts/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        // Given
        AccountRequest request = new AccountRequest("newuser", "password123", "new@example.com", "READER");
        AccountResponse response = new AccountResponse(1L, "newuser", "new@example.com", "READER", true);
        Mockito.when(accountService.createUser(Mockito.any(AccountRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.role").value("READER"));
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() throws Exception {
        // Given
        AccountRequest request = new AccountRequest("existinguser", "password123", "existing@example.com", "READER");
        Mockito.when(accountService.createUser(Mockito.any(AccountRequest.class)))
                .thenThrow(new AppException("Username already exists", ErrorCode.CONFLICT));

        // When & Then
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"));
    }

    @Test
    void testSearchUsersByUsername_Success() throws Exception {
        // Given
        String username = "john";
        AccountResponse response = new AccountResponse(1L, "john", "john@example.com", "ADMIN", true);
        Mockito.when(accountService.getUserByUsername(username)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/accounts/search")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void testSearchUsersByUsername_NoResults() throws Exception {
        // Given
        String username = "nonexistent";
        Mockito.when(accountService.getUserByUsername(username))
                .thenThrow(new AppException("No users found with username containing: " + username, ErrorCode.NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/accounts/search")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No users found with username containing: nonexistent"));
    }

}
