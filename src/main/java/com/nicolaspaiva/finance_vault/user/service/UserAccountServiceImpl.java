package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.repository.BankAccountRepository;
import com.nicolaspaiva.finance_vault.exception.UserNotFoundException;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    private final BankAccountRepository bankAccountRepository;

    @Override
    public UserAccountDto getUserProfile(String email) {

        Optional<UserEntity> user = userRepository.findByEmail(email);

        // Handles cases where the user does not exist
        if(user.isEmpty()){
            throw new UserNotFoundException("User not found");
        }

        List<TransactionDetailsDto> deposits = user.get().getAccount().getDeposits()
                .stream().map(TransactionEntity::entityToTransactionDetails).collect(Collectors.toList());

        List<TransactionDetailsDto> withdrawals = user.get().getAccount().getWithdrawals()
                .stream().map(TransactionEntity::entityToTransactionDetails).collect(Collectors.toList());

        withdrawals.forEach(withdrawal -> withdrawal.setAmount(withdrawal.getAmount() * -1));

        return UserAccountDto.builder()
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .balance(user.get().getAccount().getBalance())
                .deposits(deposits)
                .withdrawals(withdrawals)
                .build();
    }


    public void activateUser(UserEntity user){
        user.setActive(true);
    }


    public void saveUser(UserEntity user){
        userRepository.save(user);
    }


    public Optional<UserEntity> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }


    public int getUserIdByEmail(String email){

        Optional<BankAccountEntity> account =
                bankAccountRepository.findBankAccountEntityByOwnerEmail(email);

        if(account.isEmpty()){
            throw new RuntimeException("User does not exist");
        }

        return account.get().getId();
    }
}
