package com.nicolaspaiva.finance_vault.auth.service;

import com.nicolaspaiva.finance_vault.auth.confirmationtoken.dto.ConfirmationTokenResponse;
import com.nicolaspaiva.finance_vault.auth.dto.*;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.ConfirmationToken;
import com.nicolaspaiva.finance_vault.auth.confirmationtoken.service.ConfirmationTokenService;
import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.service.BankAccountService;
import com.nicolaspaiva.finance_vault.email.EmailSender;
import com.nicolaspaiva.finance_vault.security.jwt.JwtService;
import com.nicolaspaiva.finance_vault.user.entity.Role;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import com.nicolaspaiva.finance_vault.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

    private final PasswordEncoder passwordEncoder;

    private final UserAccountService userService;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    private final BankAccountService bankAccountService;

    private final ConfirmationTokenService confirmationTokenService;

    private final UserAccountService userAccountService;

    private final EmailSender emailSender;

    // TODO: REFACTOR ALL CODE SINCE THE START OF THE EMAIL VALIDATION
    // TODO: SEND VERIFICATION TOKEN IN THE EMAIL

    /**
     * Registers a new user.
     * Password validation is
     * provided by Spring Validation.
     */
    public SignUpResponse signUp(SignUpRequest signUpRequest){

        if(emailAlreadyExists(signUpRequest.getEmail())){
            return new SignUpResponse(false, "Email already exists");
        }

        try{

            UserEntity user = createUserEntity(signUpRequest);

            BankAccountEntity bankAccount = bankAccountService.createBankAccount(user);

            user.setAccount(bankAccount);

            String token = ConfirmationToken.generateTokenString();

            ConfirmationToken confirmationToken =
                    ConfirmationToken.buildUserConfirmationToken(user, token);

            String link = "http://localhost:8080/api/v1/auth/activate-account";

            emailSender.sendConfirmationEmail(user.getEmail(),
                    buildEmail(user.getFirstName(), link, token));


            userService.saveUser(user);
            confirmationTokenService.saveConfirmationToken(confirmationToken);

        } catch (Exception e){
            e.getLocalizedMessage();
            return SignUpResponse.failed();
        }

        return SignUpResponse.success();
    }


    @Override
    @Transactional
    public ConfirmationTokenResponse verifyToken(String token){

        ConfirmationToken confirmationToken;

        Optional<ConfirmationToken> confirmationTokenOpt = confirmationTokenService.
                getConfirmationToken(token);

        if(confirmationTokenOpt.isEmpty()){
            return ConfirmationTokenResponse.invalidToken();
        } else {
            confirmationToken = confirmationTokenOpt.get();
        }

        if(confirmationToken.getConfirmedAt() != null){
            return ConfirmationTokenResponse.tokenAlreadyVerified();
        }

        if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            return ConfirmationTokenResponse.tokenExpired();
        }

        confirmationTokenService.setConfirmedAt(confirmationToken);

        userAccountService.activateUser(confirmationToken.getUser());

        userService.saveUser(confirmationToken.getUser());

        return ConfirmationTokenResponse.tokenIsValid();
    }


    /**
     * Creates a user entity based on a SignUpRequest
     */
    private UserEntity createUserEntity(SignUpRequest signUpRequest){
        return UserEntity.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode((signUpRequest.getPassword())))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }


    /**
     * Checks whether the provided email exists in the
     * database
     */
    public boolean emailAlreadyExists(String email){
        return userService.findUserByEmail(email).isPresent();
    }


    /**
     * Sends a JWT to the user in case
     * authentication is successful.
     *
     * @return a JWT token if the user is authenticated,
     * throws an exception if the user is not found.
     *
     * When the exception is thrown, it is handled by
     * the Exception Handler method, which returns
     * a UserNotFoundException object
     *
     */
    public JwtAuthenticationResponse signIn(SignInRequest request){

        authManager.authenticate(new UsernamePasswordAuthenticationToken
                (request.getEmail(), request.getPassword()));

        var user = userService.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * Generates a JWT refresh token
     */
    public JwtAuthenticationResponse generateRefreshToken(RefreshTokenRequest refreshTokenRequest){
        String userEmail = jwtService.extractUsername(refreshTokenRequest.getToken());
        UserEntity user = userService.findUserByEmail(userEmail).orElseThrow();

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)){
            var jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .refreshToken(refreshTokenRequest.getToken())
                    .build();
        }

        return null;
    }

    /**
     * Builds the email that is sent
     * to the new registered user
     */
    private String buildEmail(String name, String link, String token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n" +
                "            <p>Link will expire in 15 minutes.</p>" +
                "            <p>Here's your verification token: <strong>" + token + "</strong></p>" +
                "            <p>If the link does not work, you can enter this token manually in the app.</p>" +
                "            <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
