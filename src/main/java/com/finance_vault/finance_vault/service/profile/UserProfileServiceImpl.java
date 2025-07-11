package com.finance_vault.finance_vault.service.profile;

import com.finance_vault.finance_vault.dto.profile.EmailChangeRequest;
import com.finance_vault.finance_vault.dto.profile.PasswordChangeRequest;
import com.finance_vault.finance_vault.dto.profile.ProfileDataChangeResponse;
import com.finance_vault.finance_vault.dto.profile.ProfileDataDTO;
import com.finance_vault.finance_vault.exception.InvalidProfileChangeException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.finance_vault.finance_vault.utils.Utils.isPasswordValid;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserService userService;

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
        if (!request.getNewEmail().equals(request.getNewEmailConfirmation())) {
            throw InvalidProfileChangeException.emailMustMatch();
        }

        userService.changeUserEmail(user, request.getNewEmail());

        return ProfileDataChangeResponse.successEmailChanged();
    }


    /**
     * Changes the user's password if the request is valid.
     * In order to be valid, both passwords must match and must have
     * a valid format.
     */
    @Override
    public ProfileDataChangeResponse changeUserPassword(User user, PasswordChangeRequest request) {
        if (!request.getOldPassword().matches(user.getPassword())) {
            throw InvalidProfileChangeException.invalidOldPassword();
        }

        if (!request.getNewPassword().equals(request.getNewPasswordConfirmation())) {
            throw InvalidProfileChangeException.passwordMustMatch();
        }

        userService.changeUserPassword(user, request.getNewPassword());

        return ProfileDataChangeResponse.successPasswordChanged();
    }

}
