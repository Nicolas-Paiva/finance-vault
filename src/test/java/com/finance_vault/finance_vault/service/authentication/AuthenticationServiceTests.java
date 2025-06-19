package com.finance_vault.finance_vault.service.authentication;

import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.UserRegistrationRequest;
import com.finance_vault.finance_vault.exception.InvalidRegistrationException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void AuthService_RegisterUser_WhenDataIsValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setEmail("abc@abc.com");
        request.setPassword("N1234567.");
        request.setName("Nicolas");
        request.setCurrency("EUR");


        // Mocks the methods called by userRepository
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(Mockito.any(User.class))).thenReturn(null);

        // Act
        RegistrationResponse response = authenticationService.register(request);

        // Assert
        Assertions.assertThat(response.isCreated()).isEqualTo(true);
    }


    @Test
    public void Registration_ShouldThrowException_WhenEmailIsNotValid() {

        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setEmail("abcasdas");
        request.setPassword("N1234567.");
        request.setName("Nicolas");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Invalid email format");
    }

    @Test
    public void Registration_ShouldThrowException_WhenPaswordIsNotValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setEmail("abc@abc.com");
        request.setPassword("12345678");
        request.setName("Nicolas");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Password must contain at least 8 characters," +
                        " 1 uppercase letter and one symbol (#,._@)");

    }


    @Test
    public void Registration_ShouldThrowException_WhenCurrencyIsNotValid() {
        // Arrange
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Nicolas");
        request.setEmail("abc@abc.com");
        request.setPassword("A12345678.");
        request.setName("Nicolas");
        request.setCurrency("ABC");


        // Act & Assert
        InvalidRegistrationException exception = assertThrows(InvalidRegistrationException.class,
                () -> authenticationService.register(request));

        Assertions.assertThat(exception.getMessage())
                .contains("Please provide a supported currency");

    }

}
