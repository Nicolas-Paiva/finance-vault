package com.finance_vault.finance_vault.service.profile;

import com.finance_vault.finance_vault.dto.profile.*;
import com.finance_vault.finance_vault.exception.InvalidProfileChangeException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.security.jwt.JWTService;
import com.finance_vault.finance_vault.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserService userService;

    private final JWTService jwtService;


    @Override
    public ProfileDataDTO getUserProfileData(User user) {
        return new ProfileDataDTO(user.getName(), user.getLastName(), user.getEmail());
    }


    /**
     * Changes the user's email if the EmailChangeRequest is valid.
     * In order to be valid, both email provided must match and
     * also have a valid format
     */
    @Override
    public ProfileDataChangeResponse changeUserEmail(User user, EmailChangeRequest request) {
        String newEmail = request.getNewEmail();

        if (!newEmail.equals(request.getNewEmailConfirmation())) {
            throw InvalidProfileChangeException.emailMustMatch();
        }

        // Changes the user email
        userService.changeUserEmail(user, newEmail);

        //Generates a new JWT for the provided email
        String jwt = jwtService.generateToken(newEmail);

        return ProfileDataChangeResponse.successEmailChanged(jwt);
    }


    /**
     * Changes the user's password if the request is valid.
     * In order to be valid, both passwords must match and must have
     * a valid format.
     */
    @Override
    public ProfileDataChangeResponse changeUserPassword(User user, PasswordChangeRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw InvalidProfileChangeException.invalidOldPassword();
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            throw InvalidProfileChangeException.passwordMustMatch();
        }

        // Changes the user password
        userService.changeUserPassword(user, request.getNewPassword());

        //Generates a new JWT for the provided email
        String jwt = jwtService.generateToken(user.getEmail());

        return ProfileDataChangeResponse.successPasswordChanged(jwt);
    }


    /**
     * Changes the user's name
     */
    public ProfileDataChangeResponse changeUserName(User user, UserNameChangeRequest request) {
        if (request.getNewName().isBlank()) {
            throw InvalidProfileChangeException.invalidName();
        }

        if (request.getNewLastName().isBlank()) {
            throw InvalidProfileChangeException.invalidLastName();
        }

        userService.changeUserName(user, request.getNewName(), request.getNewLastName());

        //Generates a new JWT for the provided email
        String jwt = jwtService.generateToken(user.getEmail());

        return ProfileDataChangeResponse.successNameChanged(jwt);
    }

}
