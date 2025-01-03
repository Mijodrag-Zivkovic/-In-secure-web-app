package com.News.News;

import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;

import jakarta.validation.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("Repository Tests")
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void resetDatabase() {
        accountRepository.deleteAll(); // Clear the repository
        accountRepository.flush();     // Ensure the database state is reset
    }

    @Test
    void shouldNotAllowNullUsername() {
        UserAccount user = new UserAccount(null, "password123", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowShortUsername() {
        UserAccount user = new UserAccount("ab", "password123", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullPassword() {
        UserAccount user = new UserAccount("username", null, "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowShortPassword() {
        UserAccount user = new UserAccount("username", "short", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullEmail() {
        UserAccount user = new UserAccount("username", "password123", null, Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowInvalidEmail() {
        UserAccount user = new UserAccount("username", "password123", "invalid-email", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowDuplicateUsername() {
        UserAccount user1 = new UserAccount("username", "password123", "test1@example.com", Role.ADMIN, true);
        UserAccount user2 = new UserAccount("username", "password123", "test2@example.com", Role.ADMIN, true);

        accountRepository.saveAndFlush(user1);
        assertThrows(Exception.class, () -> accountRepository.saveAndFlush(user2));
    }

    @Test
    void shouldNotAllowDuplicateEmail() {
        UserAccount user1 = new UserAccount("user1", "password123", "test@example.com", Role.ADMIN, true);
        UserAccount user2 = new UserAccount("user2", "password123", "test@example.com", Role.ADMIN, true);

        accountRepository.saveAndFlush(user1);
        assertThrows(Exception.class, () -> accountRepository.saveAndFlush(user2));
    }

    @Test
    void shouldNotAllowNullRole() {
        UserAccount user = new UserAccount("username", "password123", "test@example.com", null, true);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullIsEnabled() {
        UserAccount user = new UserAccount("username", "password123", "test@example.com", Role.ADMIN, null);
        assertThrows(ConstraintViolationException.class, () -> accountRepository.saveAndFlush(user));
    }


}
