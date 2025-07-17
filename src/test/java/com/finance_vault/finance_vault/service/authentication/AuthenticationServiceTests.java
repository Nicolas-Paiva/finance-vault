package com.finance_vault.finance_vault.service.authentication;

import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.UserRegistrationRequest;
import com.finance_vault.finance_vault.exception.InvalidRegistrationException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.security.jwt.JWTService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void AuthService_RegisterUser_WhenDataIsValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setLastName("Paiva");
        request.setEmail("abc@abc.com");
        request.setPassword("N1234567.");
        request.setCurrency("EUR");

        // Act
        RegistrationResponse response = authenticationService.register(request);

        // Assert
        Assertions.assertThat(response.isCreated()).isEqualTo(true);
    }


    @Test
    public void ShouldThrowException_WhenEmailIsNotValid() {

        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setLastName("Paiva");
        request.setEmail("abcasdas");
        request.setPassword("N1234567.");
        request.setCurrency("CHF");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Invalid email format");
    }

    @Test
    public void ShouldThrowException_WhenPasswordIsNotValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setLastName("Paiva");
        request.setEmail("abc@abc.com");
        request.setPassword("12345678");
        request.setCurrency("CHF");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Password must contain at least 8 characters," +
                        " 1 uppercase letter and one symbol (#,._@)");

    }


    @Test
    public void ShouldThrowException_WhenCurrencyIsNotValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setLastName("Paiva");
        request.setEmail("abc@abc.com");
        request.setPassword("A12345678.");
        request.setCurrency("ABC");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Please provide a supported currency");

    }

    @Test
    public void ShouldThrowException_WhenNameIsNotProvided() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("");
        request.setEmail("abc@abc.com");
        request.setPassword("A12345678.");
        request.setCurrency("EUR");

        InvalidRegistrationException e = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(e.getMessage()).contains("Please provide a name");
    }


    @Test
    public void ShouldThrowException_WhenLastNameIsNotProvided() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setLastName("");
        request.setEmail("abc@abc.com");
        request.setPassword("A12345678.");
        request.setCurrency("EUR");

        InvalidRegistrationException e = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(e.getMessage()).contains("Please provide a last name");
    }

}
