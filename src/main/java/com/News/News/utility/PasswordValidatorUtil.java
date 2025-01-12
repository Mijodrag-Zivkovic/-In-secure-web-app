package com.News.News.utility;

import org.passay.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PasswordValidatorUtil {
    public static String validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(
                List.of(
                        new LengthRule(8, 30), // Minimum 8, maximum 16 characters
                        new CharacterRule(EnglishCharacterData.UpperCase, 1), // At least one uppercase
                        new CharacterRule(EnglishCharacterData.LowerCase, 1), // At least one lowercase
                        new CharacterRule(EnglishCharacterData.Digit, 1),     // At least one digit
                        new CharacterRule(EnglishCharacterData.Special, 1),   // At least one special character
                        new WhitespaceRule()                                   // No whitespace
                )
        );

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return null; // Password is valid
        } else {
            // Collect and return validation messages
            return String.join(", ", validator.getMessages(result));
        }
    }
}
