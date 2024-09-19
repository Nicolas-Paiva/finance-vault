package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.exception.UserNotFoundException;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    // Transactional in order to allow Hibernate to fetch user transactions
    @Override
//    @Transactional
    public UserAccountDto getUserProfile(Principal principal) {

        Optional<UserEntity> user = userRepository.findByEmail(principal.getName());

        // Handles cases where the user does not exist
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        // TODO: Sort the transactions according to when they were performed using the JPA repository methods

        List<TransactionDetailsDto> deposits = user.get().getAccount().getDeposits()
                .stream().map(TransactionEntity::entityToTransactionDetails).collect(Collectors.toList());

        List<TransactionDetailsDto> withdrawals = user.get().getAccount().getWithdrawals()
                .stream().map(TransactionEntity::entityToTransactionDetails).collect(Collectors.toList());

        withdrawals.forEach(withdrawal -> withdrawal.setAmount(withdrawal.getAmount() * -1));

        // Maybe modify this?
        return UserAccountDto.builder()
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .balance(user.get().getAccount().getBalance())
                .deposits(deposits)
                .withdrawals(withdrawals)
                .build();
    }

}
