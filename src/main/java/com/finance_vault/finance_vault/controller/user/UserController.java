package com.finance_vault.finance_vault.controller.user;

import com.finance_vault.finance_vault.dto.auth.LoginSuccessResponse;
import com.finance_vault.finance_vault.dto.user.UserDTO;
import com.finance_vault.finance_vault.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * Endpoint designed to registering a new user.
     * If the registration is successful, a registration
     * response is returned with the created property set to true,
     * as well as message displaying the username.
     *
     * If the data provided is invalid, a RegistrationResponse
     * is sent, but created is set to false. A message is also displayed,
     * depending on the error.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }


    /**
     * Endpoint designed to log a user in.
     * If the user provides the correct credentials,
     * a LoginSuccessResponse is returned,
     * otherwise, a LoginErrorResponse is returned
     * to the client
     */
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse> login(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO));
    }

}
