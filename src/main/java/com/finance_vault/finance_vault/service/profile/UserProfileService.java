package com.finance_vault.finance_vault.service.profile;

import com.finance_vault.finance_vault.dto.profile.EmailChangeRequest;
import com.finance_vault.finance_vault.dto.profile.PasswordChangeRequest;
import com.finance_vault.finance_vault.dto.profile.ProfileDataChangeResponse;
import com.finance_vault.finance_vault.dto.profile.ProfileDataDTO;
import com.finance_vault.finance_vault.model.user.User;

public interface UserProfileService {

    ProfileDataDTO getUserProfileData(User user);

    ProfileDataChangeResponse changeUserEmail(User user, EmailChangeRequest request);

    ProfileDataChangeResponse changeUserPassword(User user, PasswordChangeRequest request);
}
