package com.News.News;

import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.UserAccountRepository;

import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@DisplayName("Repository Tests")
public class UserAccountRepositoryTests {

//    @Autowired
//    private UserAccountRepository repository;
//
//    // Validator to check bean validation
//    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    private static final Validator validator = factory.getValidator();
//
//    @BeforeEach
//    void resetDatabase() {
//        repository.deleteAll(); // Clear the repository
//        repository.flush();     // Ensure the database state is reset
//    }
//
//    @Test
//    @DisplayName("Create and Fetch User Account")
//    void testCreateAndFetchUserAccount() {
//        // Create a new UserAccount
//        UserAccount user = new UserAccount("testuser", "password123", "test@example.com", Role.ADMIN, true);
//        repository.save(user);
//
//        // Fetch the user by username
//        Optional<UserAccount> fetchedUser = repository.findByUsername("testuser");
//        assertThat(fetchedUser).isPresent();
//        assertThat(fetchedUser.get().getEmail()).isEqualTo("test@example.com");
//    }
//
//    @Test
//    @DisplayName("Unique Email Constraint")
//    void testUniqueEmail() {
//        // Create two users with the same email
//        UserAccount user1 = new UserAccount("user1", "password123", "duplicate@example.com", Role.ADMIN, true);
//        UserAccount user2 = new UserAccount("user2", "password456", "duplicate@example.com", Role.ADMIN, true);
//
//        repository.save(user1);
//
//        // Try to save the second user and catch the exception
//        assertThatThrownBy(() -> repository.save(user2))
//                .hasMessageContaining("could not execute statement");
//    }
//
//    @Test
//    @DisplayName("Unique Username Constraint")
//    void testUniqueUsername() {
//        // Save first user
//        UserAccount user1 = new UserAccount("testuser", "password123", "test@example.com", Role.ADMIN, true);
//        repository.save(user1);
//
//        // Attempt to save second user with same username
//        UserAccount user2 = new UserAccount("testuser", "password123", "another@example.com", Role.ADMIN, true);
//
//        assertThatThrownBy(() -> repository.save(user2))
//                .hasMessageContaining("could not execute statement");
//    }
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void shouldNotAllowNullUsername() {
        UserAccount user = new UserAccount(null, "password123", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowShortUsername() {
        UserAccount user = new UserAccount("ab", "password123", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullPassword() {
        UserAccount user = new UserAccount("username", null, "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowShortPassword() {
        UserAccount user = new UserAccount("username", "short", "test@example.com", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullEmail() {
        UserAccount user = new UserAccount("username", "password123", null, Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowInvalidEmail() {
        UserAccount user = new UserAccount("username", "password123", "invalid-email", Role.ADMIN, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowDuplicateUsername() {
        UserAccount user1 = new UserAccount("username", "password123", "test1@example.com", Role.ADMIN, true);
        UserAccount user2 = new UserAccount("username", "password123", "test2@example.com", Role.ADMIN, true);

        userAccountRepository.saveAndFlush(user1);
        assertThrows(Exception.class, () -> userAccountRepository.saveAndFlush(user2));
    }

    @Test
    void shouldNotAllowDuplicateEmail() {
        UserAccount user1 = new UserAccount("user1", "password123", "test@example.com", Role.ADMIN, true);
        UserAccount user2 = new UserAccount("user2", "password123", "test@example.com", Role.ADMIN, true);

        userAccountRepository.saveAndFlush(user1);
        assertThrows(Exception.class, () -> userAccountRepository.saveAndFlush(user2));
    }

    @Test
    void shouldNotAllowNullRole() {
        UserAccount user = new UserAccount("username", "password123", "test@example.com", null, true);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }

    @Test
    void shouldNotAllowNullIsEnabled() {
        UserAccount user = new UserAccount("username", "password123", "test@example.com", Role.ADMIN, null);
        assertThrows(ConstraintViolationException.class, () -> userAccountRepository.saveAndFlush(user));
    }


}
