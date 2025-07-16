package com.finance_vault.finance_vault.controller.profile;

import com.finance_vault.finance_vault.dto.profile.*;
import com.finance_vault.finance_vault.exception.UserNotFoundException;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.service.profile.UserProfileService;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDataDTO> getUserProfileData(Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(userProfileService.getUserProfileData(user));
    }


    @PostMapping("/profile/name")
    public ResponseEntity<ProfileDataChangeResponse> changeProfileName(@RequestBody @Valid UserNameChangeRequest request,
                                                                       Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(userProfileService.changeUserName(user, request));
    }


    @PostMapping("/profile/email")
    public ResponseEntity<ProfileDataChangeResponse> changeProfileEmail(@RequestBody @Valid EmailChangeRequest request, Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(userProfileService.changeUserEmail(user, request));
    }


    @PostMapping("/profile/password")
    public ResponseEntity<ProfileDataChangeResponse> changeProfilePassword(@RequestBody @Valid PasswordChangeRequest request, Authentication authentication) {
        User user = userService.getUserFromEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        return ResponseEntity.ok(userProfileService.changeUserPassword(user, request));
    }

}
