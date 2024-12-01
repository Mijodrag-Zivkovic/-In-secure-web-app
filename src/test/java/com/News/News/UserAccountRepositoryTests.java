package com.News.News;

import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.UserAccountRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("Repository Tests")
public class UserAccountRepositoryTests {

    @Autowired
    private UserAccountRepository repository;

    // Validator to check bean validation
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    @BeforeEach
    void resetDatabase() {
        repository.deleteAll(); // Clear the repository
        repository.flush();     // Ensure the database state is reset
    }

    @Test
    @DisplayName("Create and Fetch User Account")
    void testCreateAndFetchUserAccount() {
        // Create a new UserAccount
        UserAccount user = new UserAccount("testuser", "password123", "test@example.com", Role.ADMIN, true);
        repository.save(user);

        // Fetch the user by username
        Optional<UserAccount> fetchedUser = repository.findByUsername("testuser");
        assertThat(fetchedUser).isPresent();
        assertThat(fetchedUser.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Unique Email Constraint")
    void testUniqueEmail() {
        // Create two users with the same email
        UserAccount user1 = new UserAccount("user1", "password123", "duplicate@example.com", Role.ADMIN, true);
        UserAccount user2 = new UserAccount("user2", "password456", "duplicate@example.com", Role.ADMIN, true);

        repository.save(user1);

        // Try to save the second user and catch the exception
        assertThatThrownBy(() -> repository.save(user2))
                .hasMessageContaining("could not execute statement");
    }

    @Test
    @DisplayName("Unique Username Constraint")
    void testUniqueUsername() {
        // Save first user
        UserAccount user1 = new UserAccount("testuser", "password123", "test@example.com", Role.ADMIN, true);
        repository.save(user1);

        // Attempt to save second user with same username
        UserAccount user2 = new UserAccount("testuser", "password123", "another@example.com", Role.ADMIN, true);

        assertThatThrownBy(() -> repository.save(user2))
                .hasMessageContaining("could not execute statement");
    }


//    @Nested
//    @DisplayName("Validation Tests")
//    class ValidationTests {
//
//        @Test
//        @DisplayName("Username Length Validation")
//        void testUsernameLength() {
//            UserAccount shortUsernameUser = new UserAccount("a", "password123", "valid@example.com", Role.ADMIN, true);
//
//            Set<ConstraintViolation<UserAccount>> violations = validator.validate(shortUsernameUser);
//            assertThat(violations).isNotEmpty();
//            assertThat(violations.iterator().next().getMessage()).isEqualTo("size must be between 3 and 50");
//        }
//
//        @Test
//        @DisplayName("Password Length Validation")
//        void testPasswordLength() {
//            UserAccount shortPasswordUser = new UserAccount("validuser", "short", "valid@example.com", Role.ADMIN, true);
//
//            Set<ConstraintViolation<UserAccount>> violations = validator.validate(shortPasswordUser);
//            assertThat(violations).isNotEmpty();
//            assertThat(violations.iterator().next().getMessage()).contains("size must be between 8");
//        }
//
//        @Test
//        @DisplayName("Email Format Validation")
//        void testEmailFormat() {
//            UserAccount invalidEmailUser = new UserAccount("validuser", "password123", "invalid-email", Role.ADMIN, true);
//
//            Set<ConstraintViolation<UserAccount>> violations = validator.validate(invalidEmailUser);
//            assertThat(violations).isNotEmpty();
//            assertThat(violations.iterator().next().getMessage()).isEqualTo("must be a well-formed email address");
//        }
//
//        @Test
//        @DisplayName("Role Enum Validation not Null")
//        void testRoleEnumNotNull() {
//            UserAccount invalidRoleUser = new UserAccount("validuser", "password123", "valid@example.com", null, true);
//
//            Set<ConstraintViolation<UserAccount>> violations = validator.validate(invalidRoleUser);
//            assertThat(violations).isNotEmpty();
//            assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
//        }
//
//
//        @Test
//        @DisplayName("'isEnabled' Field Not Null Validation")
//        void testIsEnabledNotNull() {
//            UserAccount user = new UserAccount("testuser", "password123", "test@example.com", Role.ADMIN, null);
//
//            Set<ConstraintViolation<UserAccount>> violations = validator.validate(user);
//            assertThat(violations).isNotEmpty();
//            assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
//        }
//    }
}
