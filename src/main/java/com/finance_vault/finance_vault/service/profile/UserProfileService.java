package com.finance_vault.finance_vault.service.profile;

import com.finance_vault.finance_vault.dto.profile.*;
import com.finance_vault.finance_vault.model.user.User;

public interface UserProfileService {

    ProfileDataDTO getUserProfileData(User user);

    ProfileDataChangeResponse changeUserEmail(User user, EmailChangeRequest request);

    ProfileDataChangeResponse changeUserPassword(User user, PasswordChangeRequest request);

    public ProfileDataChangeResponse changeUserName(User user, UserNameChangeRequest request);
}
