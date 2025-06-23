package com.finance_vault.finance_vault.service.profile;

import com.finance_vault.finance_vault.dto.profile.EmailChangeRequest;
import com.finance_vault.finance_vault.dto.profile.PasswordChangeRequest;
import com.finance_vault.finance_vault.dto.profile.ProfileDataChangeResponse;
import com.finance_vault.finance_vault.exception.InvalidProfileChangeException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
public class ProfileServiceTests {

    @Mock
    private UserService userService;


    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    public void ProfileService_ShouldChangeUserEmail() {
        // Arrange
        User user = new User();
        user.setEmail("nicolas@outlook.com");

        String newEmail = "abc@abc.com";
        EmailChangeRequest request = new EmailChangeRequest();
        request.setNewEmail(newEmail);
        request.setNewEmailConfirmation(newEmail);

        // Act
        ProfileDataChangeResponse response = userProfileService
                .changeUserEmail(user, request);

        // Assert
        verify(userService).changeUserEmail(user, newEmail);
        Assertions.assertThat(response.isSuccess()).isEqualTo(true);
        Assertions.assertThat(response.getMessage()).contains("Email changed successfully");
    }


    @Test
    public void ProfileService_ShouldChangeUserPassword() {
        // Arrange
        User user = new User();
        user.setEmail("nicolas@outlook.com");

        String newPassword = "Z12345698.";
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setNewPassword(newPassword);
        request.setNewPasswordConfirmation(newPassword);

        // Act
        ProfileDataChangeResponse response = userProfileService
                .changeUserPassword(user, request);

        // Assert
        verify(userService).changeUserPassword(user, newPassword);
        Assertions.assertThat(response.isSuccess()).isEqualTo(true);
    }


    @Test
    public void ProfileService_ShouldThrowException_WhenEmailIsNotValid() {
        // Arrange
        User user = new User();
        user.setEmail("nicolas@outlook.com");

        String newEmail = "abc";
        EmailChangeRequest request = new EmailChangeRequest();
        request.setNewEmail(newEmail);
        request.setNewEmailConfirmation(newEmail);

        doThrow(InvalidProfileChangeException.invalidEmail())
                .when(userService).changeUserEmail(any(User.class), eq(newEmail));

        // Act + Assert
        InvalidProfileChangeException exception = assertThrows(
                InvalidProfileChangeException.class,
                () -> userProfileService.changeUserEmail(user, request)
        );

        Assertions.assertThat(exception.getMessage())
                .contains("Please provide a valid email format");
    }


    @Test
    public void ProfileService_ShouldThrowException_WhenPasswordIsNotValid() {
        // Arrange
        User user = new User();
        user.setPassword("Z123456789.");

        String newPassword = "abc";
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setNewPassword(newPassword);
        request.setNewPasswordConfirmation(newPassword);

        doThrow(InvalidProfileChangeException.invalidPassword())
                .when(userService).changeUserPassword(any(User.class), eq(newPassword));

        // Act + Assert
        InvalidProfileChangeException exception = assertThrows(
                InvalidProfileChangeException.class,
                () -> userProfileService.changeUserPassword(user, request)
        );

        Assertions.assertThat(exception.getMessage())
                .contains("Please provide a valid password. A valid password must contain at least 8 characters," +
                        " 1 uppercase letter and 1 special character (, . @ $)");
    }


}
