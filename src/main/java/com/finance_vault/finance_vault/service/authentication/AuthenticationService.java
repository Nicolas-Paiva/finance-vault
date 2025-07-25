package com.finance_vault.finance_vault.service.authentication;

import com.finance_vault.finance_vault.dto.auth.LoginRequest;
import com.finance_vault.finance_vault.dto.auth.RegistrationResponse;
import com.finance_vault.finance_vault.dto.auth.LoginSuccessResponse;
import com.finance_vault.finance_vault.dto.auth.UserRegistrationRequest;
import com.finance_vault.finance_vault.dto.transaction.TransactionRequest;
import com.finance_vault.finance_vault.exception.InvalidRegistrationException;
import com.finance_vault.finance_vault.exception.InvalidEmailOrPasswordException;
import com.finance_vault.finance_vault.model.currency.Currency;
import com.finance_vault.finance_vault.model.user.User;
import com.finance_vault.finance_vault.repository.UserRepository;
import com.finance_vault.finance_vault.security.jwt.JWTService;
import com.finance_vault.finance_vault.service.transaction.TransactionService;
import com.finance_vault.finance_vault.utils.Utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service related to user registration
 * and login
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    private final JWTService jwtService;

    private final TransactionService transactionService;


    @Transactional
    public RegistrationResponse register(UserRegistrationRequest userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        String password = userRegistrationRequest.getPassword();
        String currency = userRegistrationRequest.getCurrency();


        // Checks whether the email format is valid
        if (!Utils.isEmailFormatValid(email)) {
            throw InvalidRegistrationException.invalidEmail();
        }

        // Checks whether the username already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw InvalidRegistrationException.userEmailAlreadyExists();
        }

        // Checks whether the name field is empty
        if (userRegistrationRequest.getName().isBlank()) {
            throw InvalidRegistrationException.invalidName();
        }

        if (userRegistrationRequest.getLastName().isBlank()) {
            throw InvalidRegistrationException.invalidLastName();
        }

        // Checks whether the password is valid
        if (!Utils.isPasswordValid(password)) {
            throw InvalidRegistrationException.invalidPassword();
        }

        // Checks whether the currency is valid
        if (!isValidCurrency(currency)) {
            throw  InvalidRegistrationException.invalidCurrency();
        }


        // Proceeds to register the user
        userRegistrationRequest.setPassword(passwordEncoder.encode(password));
        userRepository.save(UserRegistrationRequest.toUser(userRegistrationRequest));
        userRepository.flush();

        addInitialTransactions(userRegistrationRequest.getEmail());

        String jwt = jwtService.generateToken(email);

        return RegistrationResponse.success(userRegistrationRequest, jwt);
    }


    // Checks the provided currency against the valid ones
    public boolean isValidCurrency(String currencyStr) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equalsIgnoreCase(currencyStr)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Logs the user in using the AuthenticationManager,
     * issuing a JWT to the user.
     *
     * If the user fails to authenticate, an InvalidUserNameOrPassword
     * exception is thrown, which causes a LoginErrorResponse to be sent
     * to the user.
     */
    public LoginSuccessResponse login(LoginRequest loginRequest) {
        Authentication authentication;

        User user = LoginRequest.toUser(loginRequest);

        // Tries to authenticate the user
        try {
             authentication = authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken
                                    (user.getEmail(), user.getPassword()));
        } catch(Exception e) {
            throw new InvalidEmailOrPasswordException();
        }

        if (authentication.isAuthenticated()) {
            String jwt = jwtService.generateToken(user.getEmail());
            return new LoginSuccessResponse(jwt);
        }

        throw new InvalidEmailOrPasswordException();
    }

    public void addInitialTransactions(String newUserEmail) {
        // Create initial transactions for display
        TransactionRequest initialDeposit = new TransactionRequest();
        initialDeposit.setAmount(1000);
        initialDeposit.setReceiverEmail(newUserEmail);

        TransactionRequest w1 = new TransactionRequest();
        w1.setAmount(12.75f);
        w1.setReceiverEmail("johndoe@gmail.com");

        TransactionRequest w2 = new TransactionRequest();
        w2.setAmount(25);
        w2.setReceiverEmail("janedoe@gmail.com");

        TransactionRequest w3 = new TransactionRequest();
        w3.setAmount(10.75f);
        w3.setReceiverEmail("finance@vault.com");

        TransactionRequest d1 = new TransactionRequest();
        d1.setAmount(50);
        d1.setReceiverEmail(newUserEmail);

        TransactionRequest w4 = new TransactionRequest();
        w4.setAmount(5.55f);
        w4.setReceiverEmail("marcus@newmann.com");

        TransactionRequest w5 = new TransactionRequest();
        w5.setAmount(25);
        w5.setReceiverEmail("mary@linn.com");

        TransactionRequest w6 = new TransactionRequest();
        w6.setAmount(8);
        w6.setReceiverEmail("johndoe@gmail.com");

        TransactionRequest d2 = new TransactionRequest();
        d2.setAmount(25);
        d2.setReceiverEmail(newUserEmail);

        TransactionRequest w7 = new TransactionRequest();
        w7.setAmount(35.33f);
        w7.setReceiverEmail("addison@cruz.com");

        TransactionRequest w8 = new TransactionRequest();
        w8.setAmount(25);
        w8.setReceiverEmail("janedoe@gmail.com");

        TransactionRequest d3 = new TransactionRequest();
        d3.setAmount(55);
        d3.setReceiverEmail(newUserEmail);

        transactionService.createTransaction(initialDeposit, userRepository.findByEmail("finance@vault.com").orElseThrow());
        transactionService.createTransaction(w1, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(w2, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(w3, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(d1, userRepository.findByEmail("johndoe@gmail.com").orElseThrow());
        transactionService.createTransaction(w4, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(w5, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(w6, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(d2, userRepository.findByEmail("marcus@newmann.com").orElseThrow());
        transactionService.createTransaction(w7, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(w8, userRepository.findByEmail(newUserEmail).orElseThrow());
        transactionService.createTransaction(d3, userRepository.findByEmail("janedoe@gmail.com").orElseThrow());
    }

}
