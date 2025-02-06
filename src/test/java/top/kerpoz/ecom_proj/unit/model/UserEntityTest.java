package top.kerpoz.ecom_proj.unit.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import top.kerpoz.ecom_proj.model.entity.UserEntity;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Should pass validation when email is valid")
    void shouldPassValidationWhenEmailIsValid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("valid.email@example.com");

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("invalid-email");

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must be a well-formed email address");
    }

    @Test
    @DisplayName("Should pass validation when password is valid")
    void shouldPassValidationWhenPasswordIsValid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("validPassword");
        user.setEmail("valid.email@example.com");

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when password is too short")
    void shouldFailValidationWhenPasswordIsTooShort() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("username");
        user.setPassword("short");
        user.setEmail("valid.email@example.com");
        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password must be at least 6 characters");
    }

    @Test
    @DisplayName("Should fail validation when username is null")
    void shouldFailValidationWhenUsernameIsNull() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setPassword("validPassword");
        user.setEmail("valid.email@example.com");

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Username cannot be null");
    }

    @Test
    @DisplayName("Should pass validation when username is valid")
    void shouldPassValidationWhenUsernameIsValid() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("validUsername");
        user.setPassword("validPassword");
        user.setEmail("valid.email@example.com");

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should pass validation when email is empty")
    void shouldPassValidationWhenEmailIsNull() {
        // Arrange
        UserEntity user = new UserEntity();
        user.setUsername("validUsername");
        user.setPassword("validPassword");
        user.setEmail(null);

        // Act
        Set<jakarta.validation.ConstraintViolation<UserEntity>> violations = validator.validate(user);

        // Assert
        assertThat(violations).isEmpty();
    }
}