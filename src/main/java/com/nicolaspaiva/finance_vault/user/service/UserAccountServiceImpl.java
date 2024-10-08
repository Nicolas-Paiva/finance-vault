package com.nicolaspaiva.finance_vault.user.service;

import com.nicolaspaiva.finance_vault.bankaccount.entity.BankAccountEntity;
import com.nicolaspaiva.finance_vault.bankaccount.repository.BankAccountRepository;
import com.nicolaspaiva.finance_vault.exception.UserNotFoundException;
import com.nicolaspaiva.finance_vault.transaction.dto.response.TransactionDetailsDto;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionEntity;
import com.nicolaspaiva.finance_vault.transaction.entity.TransactionType;
import com.nicolaspaiva.finance_vault.user.repository.UserRepository;
import com.nicolaspaiva.finance_vault.user.dto.UserAccountDto;
import com.nicolaspaiva.finance_vault.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountServiceImpl implements UserAccountService {

    private final UserRepository userRepository;

    private final BankAccountRepository bankAccountRepository;

    /**
     * Retrieves the user profile,
     * which contains the information
     * displayed in the user's home page
     */
    @Override
    public UserAccountDto getUserProfile(String email) {

        UserEntity user;
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        // Handles cases where the user does not exist
        if(userOpt.isEmpty()){
            throw new UserNotFoundException("User not found");
        } else {
            user = userOpt.get();
        }

        List<TransactionDetailsDto> deposits =
                getAccountTransactions(user, TransactionType.DEPOSIT);

        List<TransactionDetailsDto> withdrawals =
                getAccountTransactions(user, TransactionType.WITHDRAWAL);

//        withdrawals.forEach(withdrawal -> withdrawal.setAmount(withdrawal.getAmount() * -1));

        return UserAccountDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .balance(user.getAccount().getBalance())
                .deposits(deposits)
                .withdrawals(withdrawals)
                .build();
    }

    /**
     *
     */
    private List<TransactionDetailsDto> getAccountTransactions
    (UserEntity user, TransactionType transactionType){

        if(transactionType.equals(TransactionType.WITHDRAWAL)){
            List<TransactionDetailsDto> withdrawals = user.getAccount().getWithdrawals()
                    .stream().map(TransactionEntity::entityToTransactionDetails).toList();

            withdrawals.forEach(withdrawal -> withdrawal.setAmount(withdrawal.getAmount() * -1));

            return withdrawals;
        }

        return user.getAccount().getDeposits()
                .stream().map(TransactionEntity::entityToTransactionDetails).toList();
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
            return -1;
        }

        return account.get().getId();
    }

    public Optional<BankAccountEntity> getBankAccountByEmail(String email){
        return bankAccountRepository.findBankAccountEntityByOwnerEmail(email);
    }
}
