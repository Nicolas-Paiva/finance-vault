package com.finance_vault.finance_vault.controller.auth;

import com.finance_vault.finance_vault.dto.auth.LoginRequest;
import com.finance_vault.finance_vault.dto.auth.LoginSuccessResponse;
import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.UserRegistrationRequest;
import com.finance_vault.finance_vault.service.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User controller", description = "Used for authentication")
public class AuthController {

    private final AuthenticationService authenticationService;


    /**
     * Endpoint designed to registering a new user.
     * If the registration is successful, a registration
     * response is returned with the created property set to true,
     * as well as message displaying the username.
     * <p>
     * If the data provided is invalid, a RegistrationResponse
     * is sent, but created is set to false. A message is also displayed,
     * depending on the error.
     */
    @PostMapping("/auth/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(authenticationService.register(userRegistrationRequest));
    }


    /**
     * Endpoint designed to log a user in.
     * If the user provides the correct credentials,
     * a LoginSuccessResponse is returned,
     * otherwise, a LoginErrorResponse is returned
     * to the client
     */
    @PostMapping("/auth/login")
    public ResponseEntity<LoginSuccessResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

}
